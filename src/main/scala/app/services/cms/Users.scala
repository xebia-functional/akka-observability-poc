package app.services

import akka.Done
import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.kafka.{ProducerMessage, ProducerSettings}
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.google.inject.TypeLiteral
import org.apache.kafka.clients.producer.ProducerRecord

import javax.inject.Inject
import scala.concurrent.Future
import scala.util.Random

trait Users {
  def sendId(username: String): Future[Done]
  def random(): Future[Done]
}

class UsersProducers @Inject() (
  producerSettings: ProducerSettings[String, String]
)(implicit system: ActorSystem[_])
  extends Users {

  private def createMessage(username: String) = {
    val record = new ProducerRecord[String, String]("users", username)
    ProducerMessage.single(record, akka.NotUsed)
  }

  private def logResult
    : ProducerMessage.Results[String, String, akka.NotUsed.type] => Unit = {
    case ProducerMessage.Result(metadata, ProducerMessage.Message(record, _)) =>
      system.log.debug(
        s"Message ${record.value()} sent to topic ${metadata.topic()} partition ${metadata
          .partition()} with offset ${metadata.offset()}"
      )
    case _ =>
      system.log.debug("Message send failed")
  }

  def sendId(username: String): Future[Done] =
    Source
      .single(createMessage(username))
      .via(Producer.flexiFlow(producerSettings))
      .map(logResult)
      .run()

  def random(): Future[Done] = {
    Source(1 to 20)
      .map(_ => Random.between(1, 100))
      .map(num => createMessage(num.toString))
      .via(Producer.flexiFlow(producerSettings))
      .map(logResult)
      .run()
  }

}
