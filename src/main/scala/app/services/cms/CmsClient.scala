package app.services.cms

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import app.services.cms.models.RegionalSettings
import com.ncl.common.http.HttpClient
import com.ncl.common.http.adapter.Adapter
import com.ncl.common.http.models.{Request, Response}
import com.typesafe.scalalogging.LazyLogging

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CmsClient @Inject() (settings: CmsClientSettings, conn: Adapter)
  (implicit ec: ExecutionContext, system: ActorSystem[_], materializer: Materializer)
  extends HttpClient(settings, conn) with LazyLogging {

    def findRegionalSettings(region: String): Future[Option[RegionalSettings]] =
      get("/api/cms/v1/regional-settings")
        .withParameter("region", region)
        .sendAndProcess(cache = true)(
          StatusCodes.OK -> (resp => Some(resp.jsonModel[RegionalSettings])),
          StatusCodes.NotFound -> (_ => None)
        )

}
