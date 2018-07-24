package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery}
import play.api.mvc._
import play.api.libs.json.{Format, JsValue, Json}
import play.api.{Configuration, Logger}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration) extends InjectedController {

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
    var configPath: String = null
    var result: Boolean = false
    if (config.has("enforcmentEngine.preprocessing.configPath")) {
      configPath = config.get[String]("enforcmentEngine.preprocessing.configPath")
      result = com.ibm.research.storage.policy.enforcement.preprocessing.spark.PreProcessing.doPreprocessing(configPath)
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
    var configPath:String = null
    var result: String = "Missing config Parameter"
    if (config.has("enforcmentEngine.runtime.configPath")) {
      configPath = config.get[String]("enforcmentEngine.runtime.configPath")
      val accessType = queryObject.access
      val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      result = com.ibm.research.storage.policy.enforcement.sql.runtime.SparkSqlRuntime.getCompliantResult(configPath,
        purpose, accessType, query, "requester.id", requesterId)
      if (result.equals("")) {
        Future.successful(BadRequest("run time phase failed"))
      } else {
        //Future.successful(Ok(Json.toJson(response)))
        Future.successful(Ok(Json.toJson(result)))
      }
    } else {
      Future.successful(NotFound("Missing config file"))
    }
  }
}
