package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery}
import play.api.mvc._
import play.api.libs.json.Json
import play.api.{Configuration, Logger}
import java.nio.file.Paths

import bootstrap.Init

import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration,  initService: Init) extends InjectedController {

  @ApiOperation(nickname = "getCompliantDataResult",
    value = "Get Compliant result with enforcement options",
    notes = "",
    response = classOf[models.ResponseQuery], responseContainer = "List", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      value = "Request query",
      required = true,
      dataType = "models.RequestQuery",
      paramType = "body"
    )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 404, message = "Access purpose not found")))
  def getCompliantDataResult = Action.async(parse.json[RequestQuery]) { request =>

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
      //lazy
      if (config.has("enforcmentEngine.runtime.configFullPath"))
        try{
          result = com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.getCompliantResult(configFullPath,purpose, accessType, query, "requester.id", requesterId)
        }catch {
          case e: Exception => result = ""
        }

      if (result.equals(""))
        Future.successful(BadRequest("run time phase failed"))
      else {
        Future.successful(Ok(Json.toJson(result)))
      }
    } else {
      Future.successful(NotFound("Missing config file"))
    }
  }

  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 404, message = "Access purpose not found")))
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
          case e: Exception => result = ""
        }



      if (result.equals(""))
        Future.successful(BadRequest("run time phase failed"))
      else {
//        println (result)
        //Future.successful(Ok(Json.toJson(result)))
        Future.successful(Ok(result))
      }
    } else {
      Future.successful(NotFound("Missing config file"))
    }
  }
}
