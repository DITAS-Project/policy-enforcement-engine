/**
 * Copyright 2018 IBM
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package bootstrap

import scala.concurrent.Future


import javax.inject.Inject
import javax.inject.Singleton
import play.api.Configuration
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import org.apache.spark.sql.SparkSession
@Singleton
class Init @Inject() (lifecycle: ApplicationLifecycle, config: Configuration) {

  Logger.info("Starting Enforcement Engine")
  private var sparkSession = SparkSession.builder
    .master(config.get[String]("spark.master"))
    .appName(config.get[String]("spark.app.name"))
    .config("spark.jars", config.get[String]("spark.jars"))
    .config("spark.hadoop.fs.s3a.endpoint", config.get[String]("spark.hadoop.fs.s3a.endpoint"))
    .config("spark.hadoop.fs.s3a.access.key", config.get[String]("spark.hadoop.fs.s3a.access.key"))
    .config("spark.hadoop.fs.s3a.secret.key", config.get[String]("spark.hadoop.fs.s3a.secret.key"))
    .config("spark.hadoop.fs.s3a.path.style.access", config.get[String]("spark.hadoop.fs.s3a.path.style.access"))
    .config("spark.hadoop.fs.s3a.impl", config.get[String]("spark.hadoop.fs.s3a.impl"))
    .config("spark.hadoop.fs.AbstractFileSystem.s3a.impl", config.get[String]("spark.hadoop.fs.AbstractFileSystem.s3a.impl"))
    .getOrCreate()

  lifecycle.addStopHook { () =>
    Logger.info("Stopping Enforcement Engine")
    Future.successful()
  }
  def getSparkSessionInstance = {
    sparkSession
  }
}
