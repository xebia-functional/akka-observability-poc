package app.services.cms.models

import play.api.libs.json.{Json, Reads}

object RegionalSettings {
  implicit val jsonReads: Reads[RegionalSettings] = Json.reads[RegionalSettings]
}

case class RegionalSettings (
  region: String,
  agencyId: String
)
