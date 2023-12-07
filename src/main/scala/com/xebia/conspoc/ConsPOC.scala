package com.xebia.conspoc

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.ConsumerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

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
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "consumer")

  val filter = args(1) match {
    case "even" => FilterInt.Even
    case _      => FilterInt.Odd
  }

  val config           = system.settings.config.getConfig("akka.kafka.consumer")
  val bootstrapServers = system.settings.config.getString("bootstrapServers")
  val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(bootstrapServers)
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val (control, streamCompletion): (Consumer.Control, Future[akka.Done]) =
    Consumer
      .plainSource(consumerSettings, Subscriptions.topics("users"))
      .filter(record => filter(record.value().toInt))
      .map(msg => println(s"Received: ${msg.value()}"))
      .toMat(Sink.ignore)(Keep.both)
      .run()

  // Keep the application alive until user presses ENTER
  println("Consumer running. Press ENTER to stop.")
  StdIn.readLine()

  // Shutdown logic
  control
    .shutdown()
    .onComplete { _ =>
      system.terminate()
    }(system.executionContext)
}
