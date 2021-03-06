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
package controllers

import bootstrap.Init
import io.swagger.annotations._
import javax.inject.Inject
import models.{EncryptionProperty, RequestQuery}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc._
import play.api.{Configuration, Logger}
import policy.enforcement.engine.{PolicyEnforcementEngineInterface, RewrittenQueryResponse}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration,  initService: Init) extends InjectedController {
  private val LOGGER = LoggerFactory.getLogger("EnforcementEngine")

  @ApiOperation(nickname = "rewriteSQLQuery",
    value = "Get rewritten SQL query and list of tables names to extract the compilant result.",
    notes = "This method returns a json object which contains the rewritten SQL query and an array of table names",
    response = classOf[models.ResponseQuery], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Request query", required = true, dataType = "models.RequestQuery", paramType = "body")))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 500, message = "Error processing result")))
  def rewriteSQLQuery = Action.async(parse.json[RequestQuery]) { request =>

    val authHeader = request.headers.get("authorization")
    val queryObject = request.body
    val query = queryObject.query
    var purpose = queryObject.purpose
    val requesterId = queryObject.requesterId
    var configFullPath: String = null

    if (config == null) {
      LOGGER.error("Missing application.conf, existing EnforcementEngine");
      Future.successful(BadRequest("Missing application.conf"))
    } else if (!config.has("enforcementEngine.className")) {
      LOGGER.error("Missing enforcement engine name, existing EnforcementEngine");
      Future.successful(BadRequest("Missing enforcement engine name"))
    } else if (!config.has("enforcementEngine.runtime.configFullPath")) {
      LOGGER.error("Missing config file, existing EnforcementEngine");
      Future.successful(BadRequest("Missing config file"))
    } else {
      var result: policy.enforcement.engine.RewrittenQueryResponse = null

      configFullPath = config.get[String]("enforcementEngine.runtime.configFullPath")

      val accessType = queryObject.access
      val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      val spark = initService.getSparkSessionInstance

      var enforcementEngine: policy.enforcement.engine.PolicyEnforcementEngineInterface = null
      //RewrittenQueryResponse object which is the enforcement engine response contains the rewritten query
      //and an array of tables names to apply the query on in order to get the compilant result.
      val name: String = config.get[String]("enforcementEngine.className")

      enforcementEngine = Class.forName(name).newInstance().asInstanceOf[policy.enforcement.engine.PolicyEnforcementEngineInterface]
      val encryptionProperties = fillEncryptionProperties(authHeader, enforcementEngine, purpose, accessType)
      setEncryptionProperties(spark, encryptionProperties)
      val originalPurpose = purpose
      if (!"data_movement_public_cloud".equals(purpose) || !"write".equals(accessType)) {
        try {
          if ("data_movement_public_cloud".equals(purpose)) {
            purpose = "Research"
          }
          result = enforcementEngine.getNewQuery(spark, configFullPath, purpose, accessType, query, "requester.id", requesterId)
          purpose = originalPurpose
        } catch {
          case e: Exception => result = null; LOGGER.error("Failed to rewrite the query", e);
        }
      }
      if (! "data_movement_public_cloud".equals(purpose) && (result == null ||
        result.rewrittenSQLquery == null ||
        result.rewrittenSQLquery.equals("") ||
        result.tableArray.length == 0)) {
        Future.successful(InternalServerError("Failed to rewrite the query."))
      } else {
        if ("data_movement_public_cloud".equals(purpose) && "write".equals(accessType)) {
          result = new RewrittenQueryResponse
        }
        LOGGER.info("EnforcementEngine succeed to rewrite the query!")

        //construct the response
        val tables: ArrayBuffer[models.TableName] = new ArrayBuffer[models.TableName](result.tableArray.size)
        for (table <- result.tableArray) {
          val newTableName = new models.TableName(table)
          tables += newTableName
        }

        val responseQuery: models.ResponseQuery = new models.ResponseQuery(result.rewrittenSQLquery, tables, encryptionProperties)
        Future.successful(Ok(Json.toJson(responseQuery)))
      }
    }
  }

  def fillEncryptionProperties(authHeader: Option[String], enforcementEngine: PolicyEnforcementEngineInterface,
                                purpose: String, accessType: String): ArrayBuffer[EncryptionProperty]  = {
    val token: String = authHeader.getOrElse("").replace("Bearer ", "")
    val policyEngineParametersMap: mutable.Map[String, String] =
      mutable.Map[String, String]("configFile" -> config.get[String]("enforcementEngine.runtime.credentialsFullPath"),
        "locationConfigFile" -> config.get[String]("enforcementEngine.runtime.connectionsConfigFullPath"))
    val kmsInstanceUrl = config.getOptional[String]("kmsInstanceURL")

    // configFullPath
    val schema: StructType = null
    val dataSetStoragePath: String = "TODO_path"
    var kmsClass: String = null
    if (kmsInstanceUrl.getOrElse("NONE").startsWith("http")) {
      kmsClass = "com.ibm.parquet.key.management.VaultClient"
    } else {
      kmsClass = "com.ibm.parquet.key.management.LocalKMS"
    }
    val sessionEncryptionProperties = enforcementEngine.getCryptoSessionProperties(token, kmsClass, kmsInstanceUrl.getOrElse("NONE"),
      policyEngineParametersMap)
    val datasetEncryptionProperties = enforcementEngine.getDatasetEncryptionProperties(schema, dataSetStoragePath, purpose, accessType): mutable.Map[String, String]
    val encryptionPropertiesMap: mutable.Map[String, String] = sessionEncryptionProperties ++ datasetEncryptionProperties
    val encryptionPropertiesSeq = encryptionPropertiesMap.map(entry => new EncryptionProperty(entry._1, entry._2))
    val encryptionProperties: ArrayBuffer[EncryptionProperty] = new ArrayBuffer[EncryptionProperty]()
    encryptionProperties.insertAll(0, encryptionPropertiesSeq)
    encryptionProperties
  }

  def setEncryptionProperties(spark: SparkSession, encryptionProperties: ArrayBuffer[EncryptionProperty]) = {
    val hadoopConfig = spark.sparkContext.hadoopConfiguration
    for (encryptionProperty <- encryptionProperties) {
      val key = (encryptionProperty.key)
      val value = (encryptionProperty.value)
      hadoopConfig.set(key, value)
    }
  }
}
