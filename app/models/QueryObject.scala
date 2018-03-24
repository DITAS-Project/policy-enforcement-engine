package models

import io.swagger.annotations._
import play.api.mvc.{ Action, BodyParsers, Controller }
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.libs.json.Reads


object QueryObject {
  //  implicit val queryObjectFormat = Json.format[QueryObject]
  implicit val queryObjectWrites: Writes[QueryObject] = Json.writes[QueryObject]
  implicit val queryObjectReads: Reads[QueryObject] = Json.reads[QueryObject]
}


case class QueryObject(
  @ApiModelProperty(value="SQL query") query:       String,
  @ApiModelProperty(value="Access purpose") purpose:     String,
  @ApiModelProperty(value="RequestorId") requester:   String,
  @ApiModelProperty(value="VDC Blueprint ID") blueprintId: String) 

