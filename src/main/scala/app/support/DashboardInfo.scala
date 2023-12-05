package app.support

import app.support.DashboardInfo.Settings
import com.ncl.common.server.config.{Configuration, Configurator}
import com.ncl.common.server.routing.tools.CustomToolsDashboard
import play.api.libs.json.{Json, JsValue, Writes}

import javax.inject.Inject

object DashboardInfo extends Configurator {

  case class Settings(
    cmsHost: String
  )

  def settingsFromConfig(config: Configuration): Settings = Settings(
    cmsHost = config.get[String]("cms.host")
  )

}

class DashboardInfo @Inject() (settings: Settings) extends CustomToolsDashboard {
  implicit val jsonWrites: Writes[Settings] = Json.writes[Settings]
  override def toJson: JsValue = Json.toJson(settings)
}
