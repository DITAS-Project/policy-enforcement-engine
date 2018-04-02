package controllers

import scala.concurrent.Future


import io.swagger.annotations._
import play.api.mvc.{Action, BodyParsers, Controller}
import bootstrap.Init
import javax.inject._
import javax.inject.Inject
import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerHelpers
import play.mvc.BodyParser
import play.api.Logger

import models.QueryObject

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() (config: Configuration, initService: Init, parser: BodyParser.Default)(val messagesApi: MessagesApi) extends ControllerHelpers {


  // TODO add request and requester attributes
  @ApiImplicitParams(Array(
      new ApiImplicitParam(
          value = "Rewrite SQL query with enforcement options",
          required = true,
          dataType = "models.QueryObject",
          paramType="body"
          )
      ))
  @ApiResponses(Array(
     new ApiResponse(code=400, message="Invalid parameters supplied"),
     new ApiResponse(code=404, message="Access purpose not found")))
  def rewriteSQLQuery = Action.async(BodyParsers.parse.json[QueryObject]) { request =>
  
    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      var newQuery = query
      // OK(Json.toJson(queryObject))
      Future.successful(Ok(Json.toJson(newQuery)))
    
  }

def redirectDocs = Action {
  Redirect(url = "/web/public/main/lib/swagger-ui/index.html", queryString = Map("url" -> Seq("/swagger.json")))
// Redirect("/assets/lib/swagger-ui/index.html?/url=/swagger.json")
}
  
}


