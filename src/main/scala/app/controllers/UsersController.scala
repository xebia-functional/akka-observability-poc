package app.controllers

import akka.http.scaladsl.server.Route
import app.services.Users
import com.ncl.common.server.routing.Controller
import com.ncl.common.server.routing.response.JsonResponse

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UsersController @Inject() (users: Users)(implicit ec: ExecutionContext)
  extends Controller {

  val routes: Route = concat(
    usersRoute
  )

  private def usersRoute: Route = path("v1" / "users") {
    concat(
      post {
        action("send-user-id") { params =>
          val userId = params.get[String]("userId")
          users.sendId(userId).map(_ => JsonResponse.ok("sent"))
        }
      },
      path("random") {
        post {
          action("send-random-user-ids") { _ =>
            users.random().map(_ => JsonResponse.ok("sent random user ids"))
          }
        }
      }
    )
  }

}
