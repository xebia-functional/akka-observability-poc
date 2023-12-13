package app

import akka.kafka.ProducerSettings
import app.services.Users
import app.services.UsersProducers
import app.services.cms.CmsClientSettings
import app.support.DashboardInfo
import com.ncl.common.http.adapter.{Adapter, DefaultAdapter}
import com.ncl.common.server.config.Configuration
import com.ncl.common.server.service.{ApplicationMain, ApplicationSetup}
import org.apache.kafka.common.serialization.StringSerializer

object Main extends ApplicationMain {

  boot(
    new ApplicationSetup {

      override def setup(): Unit = {
        bind(classOf[Adapter]).toInstance(new DefaultAdapter())
        // Services
        // TODO: Service binding here
        // Configurations
        bind(classOf[DashboardInfo.Settings]).toInstance(
          DashboardInfo.settingsFromConfig(config)
        )
        bind(classOf[CmsClientSettings]).toInstance(
          CmsClientSettings.fromConfig(config.get[Configuration]("cms"))
        )

        // Users deps and services.
        bind(classOf[ProducerSettings[String, String]]).toInstance(
          ProducerSettings(
            config.underlying,
            new StringSerializer(),
            new StringSerializer()
          ).withBootstrapServers(config.get[String]("bootstrapServers"))
        )
        bind(classOf[Users]).to(classOf[UsersProducers])
        ()
      }

    }
  )

}
