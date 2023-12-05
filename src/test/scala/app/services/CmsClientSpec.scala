package app.services

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import app.BaseAkkaTest
import app.services.cms.{CmsClient, CmsClientSettings}
import app.services.cms.models.RegionalSettings
import com.ncl.common.http.adapter.SingleStrictResponseMockAdapter
import com.ncl.common.http.settings.{CacheSettings, CircuitBreakerSettings}
import play.api.libs.json.{Json, JsValue}

import scala.concurrent.duration._

class CmsClientSpec extends BaseAkkaTest {

  def create(
    status: StatusCode,
    content: JsValue
  ): CmsClient = {
    val settings: CmsClientSettings = CmsClientSettings(
      host = "http://localhost",
      requestor = "",
      timeout = 1.second,
      cache = CacheSettings.noCache,
      circuitBreaker = CircuitBreakerSettings(enabled = false)
    )
    new CmsClient(
      settings,
      SingleStrictResponseMockAdapter.json(status = status, content = content)
    )
  }

  "findRegionalSettings when exists" in {
    val subject = create(
      status = StatusCodes.OK,
      content = Json.obj("region" -> "www.ncl.com", "agencyId" -> "12345")
    )
    subject.findRegionalSettings("www.ncl.com").futureValue shouldBe
      Some(RegionalSettings(region = "www.ncl.com", agencyId = "12345"))
  }

  "findRegionalSettings when does not exists" in {
    val subject = create(
      status = StatusCodes.NotFound,
      content = Json.obj()
    )
    subject.findRegionalSettings("www.ncl.com").futureValue shouldBe None
  }


}
