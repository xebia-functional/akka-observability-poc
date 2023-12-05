package app

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.stream.Materializer
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.ExecutionContext

abstract class BaseAkkaTest extends BaseTest with BeforeAndAfterAll {

    val testKit: ActorTestKit = ActorTestKit()
    implicit val system: ActorSystem[_] = testKit.internalSystem
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContext = system.executionContext


    override protected def afterAll(): Unit = testKit.shutdownTestKit()
}
