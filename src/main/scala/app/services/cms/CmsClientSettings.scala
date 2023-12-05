package app.services.cms

import com.ncl.common.http.settings.{CacheSettings, CircuitBreakerSettings, ClientSettings}
import com.ncl.common.server.config.{Configuration, Configurator}

import scala.concurrent.duration.FiniteDuration

object CmsClientSettings extends Configurator {

  def fromConfig(config: Configuration): CmsClientSettings = CmsClientSettings(
    host = config.get[String]("host"),
    requestor = config.getOpt[String]("requestor").mkString,
    timeout = config.get[FiniteDuration]("timeout"),
    cache = CacheSettings.fromConfig(config.get[Configuration]("cache").underlying),
    circuitBreaker = CircuitBreakerSettings.fromConfig(config.get[Configuration]("circuit-breaker").underlying)
  )

}

case class CmsClientSettings(
  host: String,
  requestor: String,
  timeout: FiniteDuration,
  cache: CacheSettings,
  circuitBreaker: CircuitBreakerSettings
) extends ClientSettings
