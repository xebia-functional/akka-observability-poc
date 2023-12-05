package app

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._

trait BaseTest extends AnyWordSpecLike with Matchers with ScalaFutures {
    override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 2.seconds)
}
