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

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery, TableName}
import play.api.mvc._
import play.api.libs.json._
import play.api.{Configuration, Logger}
import java.nio.file.Paths
import org.slf4j.LoggerFactory
import bootstrap.Init
import scala.collection.mutable.ArrayBuffer


import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration,  initService: Init) extends InjectedController {
  private val LOGGER = LoggerFactory.getLogger("EHealthVDCController")

  @ApiOperation(nickname = "rewriteSQLQuery",
    value = "Get rewritten SQL query and list of tables names to extract the compilant result from",
    notes = "This method returns a json object which contains the rewritten SQL query and an array of table names",
    response = classOf[models.ResponseQuery], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Request query", required = true, dataType = "models.RequestQuery", paramType="body")))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 500, message = "Error processing result")))
  def rewriteSQLQuery = Action.async(parse.json[RequestQuery]) { request =>

    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val requesterId = queryObject.requesterId
    var configFullPath:String = null

    //RewrittenQueryResponse object which is the enforcement engine response contains the rewritten query
    //and an array of tables names to apply the query on in order to get the compilant result.
    var result: com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.RewrittenQueryResponse = null

    if (config != null && config.has("enforcmentEngine.runtime.configFullPath")) {
      configFullPath = config.get[String]("enforcmentEngine.runtime.configFullPath")
      val accessType = queryObject.access
      val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      val spark = initService.getSparkSessionInstance

      if (config.has("enforcmentEngine.runtime.configFullPath"))
        try{
          result = com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.getNewQuery(spark,
            configFullPath,purpose, accessType, query, "requester.id", requesterId)
        }catch {
          case e: Exception => result = null; LOGGER.error("Failed to rewrite the query", e);
        }
      if (result == null ||
        result.rewrittenSQLquery == null ||
        result.rewrittenSQLquery.equals("") ||
        result.tableArray.length == 0)
        Future.successful(InternalServerError("Failed to rewrite the query"))
      else {
        LOGGER.info("EnforcementEngine succeed to rewrite the query!")

        //construct the response
        val tables : ArrayBuffer[models.TableName] = new ArrayBuffer[models.TableName](result.tableArray.size)
        for (table <- result.tableArray) {
          val newTableName = new models.TableName (table)
          tables += newTableName
        }
        val responseQuery: models.ResponseQuery = new models.ResponseQuery (result.rewrittenSQLquery, tables)

        Future.successful(Ok(Json.toJson(responseQuery)))
      }
    } else {
      LOGGER.error("Missing config file, existing EnforcementEngine");
      Future.successful(BadRequest("Missing config file"))
    }
  }
}
