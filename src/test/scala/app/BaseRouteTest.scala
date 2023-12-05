package app

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._

abstract class BaseRouteTest
    extends AnyWordSpecLike
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest
    with Directives {
    override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 2.seconds)
}
