package app.support

import akka.http.scaladsl.server.Route
import app.controllers.{SampleController, UsersController}
import com.google.inject.Inject
import com.ncl.common.server.service.ApplicationRouter

class MainRouter @Inject() (
  sampleController: SampleController,
  usersController: UsersController
) extends ApplicationRouter {

  val routes: Route = pathPrefix("api" / "service")(
    concat(
      sampleController.routes,
      usersController.routes
    )
  )

}
