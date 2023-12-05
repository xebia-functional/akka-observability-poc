package app

import app.services.cms.CmsClientSettings
import app.support.DashboardInfo
import com.ncl.common.http.adapter.{Adapter, DefaultAdapter}
import com.ncl.common.server.config.Configuration
import com.ncl.common.server.service.{ApplicationMain, ApplicationSetup}

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
      }
    }
  )
}
