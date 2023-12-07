package com.xebia.prodpoc

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.kafka.ProducerMessage
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.util.Random

object ProdPOC extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "producer")
  implicit val ec = system.executionContext

  val config = system.settings.config.getConfig("akka.kafka.producer")
  val bootstrapServers = system.settings.config.getString("bootstrapServers")

  val creator = new TopicCreator(bootstrapServers, "users", ec)
  creator.running

  val producerSettings: ProducerSettings[String, String] = ProducerSettings(config, new StringSerializer(), new StringSerializer())
    .withBootstrapServers(bootstrapServers)

  Source(1 to 20)
    .map(_ => Random.between(1, 100))
    .map{ x =>
      println(s"Preparing to send $x")
      val record: ProducerRecord[String, String] = new ProducerRecord[String, String]("users", x.toString)
      ProducerMessage.single(record, akka.NotUsed)
    }
    .via(Producer.flexiFlow(producerSettings))
    .map {
      case ProducerMessage.Result(metadata, ProducerMessage.Message(record, _)) =>
        println(s"Message ${record.value()} sent to topic ${metadata.topic()} partition ${metadata.partition()} with offset ${metadata.offset()}")
      case _ =>
        println("Message send failed")
    }
    .run()
}
