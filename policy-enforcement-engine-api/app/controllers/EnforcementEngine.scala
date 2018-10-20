package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery}
import play.api.mvc._
import play.api.libs.json._
import play.api.{Configuration, Logger}
import java.nio.file.Paths
import org.slf4j.LoggerFactory
import bootstrap.Init

import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration,  initService: Init) extends InjectedController {
  private val LOGGER = LoggerFactory.getLogger("EHealthVDCController")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 500, message = "Error processing result")))
  def rewriteSQLQuery = Action.async(parse.json[RequestQuery]) { request =>

    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val requesterId = queryObject.requesterId
    var configFullPath:String = null

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
        var responseStr: String = result.rewrittenSQLquery


        val emptyArray = Json.arr()
        var filledArray = emptyArray
        for (table <- result.tableArray) {
          val testObject:JsObject = Json.obj("name" -> table)
          filledArray = filledArray :+ testObject
        }
        val jsonResult = Json.obj(
          "rewrittenQuery" -> result.rewrittenSQLquery,
          "tables" -> filledArray)
        Json.prettyPrint(jsonResult)

        Future.successful(Ok(jsonResult))
      }
    } else {
      LOGGER.error("Missing config file, existing EnforcementEngine");
      Future.successful(BadRequest("Missing config file"))
    }
  }
}
