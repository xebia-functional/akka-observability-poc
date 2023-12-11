package com.xebia.conspoc

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.{Keep, Sink}
import com.xebia.useractorpoc.UserActor
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity
import akka.pattern.StatusReply
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Future
import scala.io.StdIn

sealed trait FilterInt {
  def apply(x: Int): Boolean
}

object FilterInt {
  final case object Even extends FilterInt {
    def apply(x: Int): Boolean = x % 2 == 0
  }

  final case object Odd extends FilterInt {
    def apply(x: Int): Boolean = !Even(x)
  }
}

object ConsPOC extends App {

  private def configWithPort(port: Int): Config =
    ConfigFactory.parseString(
      s"""
       akka.remote.artery.canonical.port = $port
        """).withFallback(ConfigFactory.load())

  val port: Int = args.lift(1) match {
    case Some(portString) if portString.matches("""\d+""") => portString.toInt
    case _ => throw new IllegalArgumentException("An akka cluster port argument is required")
  }

  val filter = args.lift(0) match {
    case Some("even") => FilterInt.Even
    case _      => FilterInt.Odd
  }

  val groupId = args.lift(0).getOrElse("group")

//  implicit val system = ActorSystem(Behaviors.empty, "consumer", configWithPort(port))
  implicit val consumerAS = ActorSystem(Behaviors.empty, "consumer")

  val shardingAS = ActorSystem[Nothing](Behaviors.empty, "Sharding", configWithPort(port))

  val sharding = ClusterSharding(shardingAS)

  sharding.init(Entity(UserActor.TypeKey) { entityContext =>
    UserActor(entityContext.entityId)
  })

  val consumerConfig   = consumerAS.settings.config.getConfig("akka.kafka.consumer")
  val bootstrapServers = consumerAS.settings.config.getString("bootstrapServers")
  val consumerSettings = ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(bootstrapServers)
    .withGroupId(groupId)
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val (control, streamCompletion): (Consumer.Control, Future[akka.Done]) =
    Consumer
      .plainSource(consumerSettings, Subscriptions.topics("users"))
      .filter(record => filter(record.value().toInt))
      .map { msg =>
        val id        = msg.value()
        val entityRef = sharding.entityRefFor(UserActor.TypeKey, id)
        entityRef.tell(UserActor.Add(id, shardingAS.deadLetters))
      }
      .toMat(Sink.ignore)(Keep.both)
      .run()

  // Keep the application alive until user presses ENTER
  println("Consumer running. Press ENTER to stop.")
  StdIn.readLine()

  // Shutdown logic
  control
    .shutdown()
    .onComplete { _ =>
      consumerAS.terminate()
    }(consumerAS.executionContext)



}
