package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}


object RequestQuery {
  implicit val requestQueryWrites: Writes[RequestQuery] = Json.writes[RequestQuery]
  implicit val requestQueryReads: Reads[RequestQuery] = Json.reads[RequestQuery]
}


case class RequestQuery(
  @ApiModelProperty(value="SQL query") query:       String,
  @ApiModelProperty(value="Access purpose") purpose:     String,
  @ApiModelProperty(value="RequestorId") requester:   String,
  @ApiModelProperty(value="VDC Blueprint ID") blueprintId: String) 

