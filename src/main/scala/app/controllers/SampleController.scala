package app.controllers

import akka.http.scaladsl.server.Route
import app.services.cms.CmsClient
import com.ncl.common.server.routing.response.JsonResponse
import com.ncl.common.server.routing.Controller
import play.api.libs.json.Json

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SampleController @Inject() (cms: CmsClient) (implicit ec: ExecutionContext) extends Controller {

  val routes: Route = concat(
    getRegionalSettings
  )

  private def getRegionalSettings: Route = path("v1" / "sample") {
    get {
      action() { params =>
        val region = params.get[String]("region")
        cms.findRegionalSettings(region).map {
          case Some(resp) => JsonResponse.ok(Json.obj("agencyId" -> resp.agencyId))
          case None => JsonResponse.notFound(Json.obj("reason" -> "Region not found"))
        }
      }
    }
  }

}
