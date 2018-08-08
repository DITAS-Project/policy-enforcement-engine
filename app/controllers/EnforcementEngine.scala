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

  @ApiOperation(nickname = "doPreProcessing",
    value = "do pre-processing phase of enforcement engine",
    notes = "",
    response = classOf[models.ResponseQuery], responseContainer = "List", httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid parameters supplied"),
    new ApiResponse(code = 404, message = "config was not found")))
  def doPreProcessing = Action.async { request =>
    val queryObject = request.body
    println (queryObject.asText)
    var configFullPath: String = null
    var result: Boolean = false;
    var currentPath = Paths.get(".").toAbsolutePath+"config/"
    if (config != null && config.has("enforcmentEngine.preprocessing.configFullPath")) {
      configFullPath = config.get[String]("enforcmentEngine.preprocessing.configFullPath")
      try {
        result = com.ibm.research.storage.policy.enforcement.preprocessing.spark.PreProcessing.doPreprocessing(configFullPath)
        //result = true
      } catch {
        case e: Exception => result = false
      }
      if (result == true)
        Future.successful(Ok("Pre-processing phase finished successfully"))
      else {
        Future.successful(BadRequest("Pre-processing phase failed"))
      }
    } else {
      Future.successful(NotFound("Missing config file"))
    }
  //  Future.successful(Ok("Pre-processing phase finished successfully"))
  }


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
          //result = "OK"
        }catch {
          case e: Exception => result = ""
        }

      if (result.equals(""))
        Future.successful(BadRequest("run time phase failed"))
      else {
        //Future.successful(Ok(Json.toJson(response)))
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
      //lazy
      if (config.has("enforcmentEngine.runtime.configFullPath"))
        try{
          result = com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.getNewQuary(configFullPath,purpose, accessType, query, "requester.id", requesterId)
          //result = "OK"
        }catch {
          case e: Exception => result = ""
        }



      if (result.equals(""))
        Future.successful(BadRequest("run time phase failed"))
      else {
        println (result)
        //Future.successful(Ok(Json.toJson(result)))
        Future.successful(Ok(result))
      }
    } else {
      Future.successful(NotFound("Missing config file"))
    }
  }
}
