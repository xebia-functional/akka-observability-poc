package app.controllers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import org.mockito.Mockito.{reset, when}
import org.mockito.MockitoSugar.mock
import app.BaseRouteTest
import app.services.cms.CmsClient
import app.services.cms.models.RegionalSettings
import play.api.libs.json.Json

import scala.concurrent.Future

class SampleControllerSpec extends BaseRouteTest {

  val cms: CmsClient = mock[CmsClient]
  val routes: Route = new SampleController(cms).routes

  "GET /v1/sample when region found" in {
    reset(cms)
    when(
      cms.findRegionalSettings("www.ncl.com")
    ).thenReturn(
      Future.successful(
        Some(
          RegionalSettings(region = "www.ncl.com", agencyId = "12345")
        )
      )
    )
    Get("/v1/sample?region=www.ncl.com") ~> routes ~> check {
      response.status shouldBe StatusCodes.OK
      responseAs[String] shouldBe Json.obj("agencyId" -> "12345").toString
    }
  }

  "GET /v1/sample when region not found" in {
    reset(cms)
    when(
      cms.findRegionalSettings("www.ncl.com")
    ).thenReturn(
      Future.successful(None)
    )
    Get("/v1/sample?region=www.ncl.com") ~> routes ~> check {
      response.status shouldBe StatusCodes.NotFound
      responseAs[String] shouldBe Json.obj("reason" -> "Region not found").toString
    }
  }

}
