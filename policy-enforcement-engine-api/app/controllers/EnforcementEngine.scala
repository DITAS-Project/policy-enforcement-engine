package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery}
import play.api.mvc._
import play.api.libs.json.Json
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
  def getRewrittenQuery = Action.async(parse.json[RequestQuery]) { request =>

    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val requesterId = queryObject.requesterId
    var configFullPath:String = null

    var result: String = ""

    if (config != null && config.has("enforcmentEngine.runtime.configFullPath")) {
      configFullPath = config.get[String]("enforcmentEngine.runtime.configFullPath")
      val accessType = queryObject.access
      val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      val spark = initService.getSparkSessionInstance
      //lazy
      if (config.has("enforcmentEngine.runtime.configFullPath"))
        try{
          result = com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.getNewQuery(spark, configFullPath,purpose, accessType, query, "requester.id", requesterId)
          //result = "OK"
        }catch {
          case e: Exception => result = ""; LOGGER.error("Exception in engine " + e);
        }
      if (result.equals("")) //TODO: make the error message more informative
        Future.successful(InternalServerError("Failure"))
      else {
        println (result)
        Future.successful(Ok(result))
      }
    } else {
      Future.successful(BadRequest("Missing config file"))
    }
  }
}
