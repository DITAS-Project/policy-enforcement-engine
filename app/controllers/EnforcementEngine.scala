package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.QueryObject
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

// TODO thread pool!!!
@Api("Enforcement Engine")
class EnforcementEngine @Inject() () extends InjectedController {


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
  def rewriteSQLQuery = Action.async(parse.json[QueryObject]) { request =>
  
    val queryObject = request.body
    val query = queryObject.query
    val purpose = queryObject.purpose
    val blueprintId = queryObject.blueprintId
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      var newQuery = query
      Future.successful(Ok(Json.toJson(newQuery)))
    
  }

def redirectDocs = Action {
  Redirect(url = "/web/public/main/lib/swagger-ui/index.html", queryString = Map("url" -> Seq("/swagger.json")))
// Redirect("/assets/lib/swagger-ui/index.html?/url=/swagger.json")
}
  
}


