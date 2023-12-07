package com.xebia.conspoc

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.{Keep, Sink}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.Future
import scala.io.StdIn

object ConsPOC extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "consumer")

  val config = system.settings.config.getConfig("akka.kafka.consumer")
  val bootstrapServers = system.settings.config.getString("bootstrapServers")
  val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(bootstrapServers)
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val (control, streamCompletion): (Consumer.Control, Future[akka.Done]) =
    Consumer
      .plainSource(consumerSettings, Subscriptions.topics("users"))
      .map(msg => println(s"Received: ${msg.value()}"))
      .toMat(Sink.ignore)(Keep.both)
      .run()

  // Keep the application alive until user presses ENTER
  println("Consumer running. Press ENTER to stop.")
  StdIn.readLine()

  // Shutdown logic
  control.shutdown().onComplete { _ =>
    system.terminate()
  }(system.executionContext)
}