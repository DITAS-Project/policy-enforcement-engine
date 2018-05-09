package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.{RequestQuery, ResponseQuery}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() () extends InjectedController {


  @ApiOperation(nickname = "rewriteSQLQuery",
    value = "Rewrite SQL query with enforcement options",
    notes = "",
    response = classOf[models.ResponseQuery], responseContainer = "List", httpMethod = "POST")
  @ApiImplicitParams(Array(
      new ApiImplicitParam(
          value = "Request query",
          required = true,
          dataType = "models.RequestQuery",
          paramType="body"
          )
      ))
  @ApiResponses(Array(
     new ApiResponse(code=400, message="Invalid parameters supplied"),
     new ApiResponse(code=404, message="Access purpose not found")))
  def rewriteSQLQuery = Action.async(parse.json[RequestQuery]) { request =>
  
    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      var newQuery = query
      val response = new ResponseQuery(newQuery)
      Future.successful(Ok(Json.toJson(response)))
    
  }

}


