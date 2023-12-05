package app.support

import akka.http.scaladsl.server.Route
import app.controllers.SampleController
import com.google.inject.Inject
import com.ncl.common.server.service.ApplicationRouter

class MainRouter @Inject() (
  sampleController: SampleController
) extends ApplicationRouter {

  val routes: Route = pathPrefix("api" / "service") (
    concat(
      sampleController.routes
    )
  )
}
