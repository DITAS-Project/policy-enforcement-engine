package bootstrap

import scala.concurrent.Future


import javax.inject.Inject
import javax.inject.Singleton
import play.api.Configuration
import play.api.Logger
import play.api.inject.ApplicationLifecycle

@Singleton
class Init @Inject() (lifecycle: ApplicationLifecycle, config: Configuration) {

  Logger.info("Starting Enforcement Engine")

  lifecycle.addStopHook { () =>
    Logger.info("Stopping Enforcement Engine")
    Future.successful()
  }

}
