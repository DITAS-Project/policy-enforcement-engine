package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}


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

