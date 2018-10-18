package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}


object RequestQuery {
  implicit val requestQueryWrites: Writes[RequestQuery] = Json.writes[RequestQuery]
  implicit val requestQueryReads: Reads[RequestQuery] = Json.reads[RequestQuery]
}


case class RequestQuery(
                         @ApiModelProperty(value="SQL query", example="SELECT name FROM data") query:       String,
                         @ApiModelProperty(value="Purpose", example="NutritionConsultation") purpose:     String,
                         @ApiModelProperty(value="Access type", example="read") access:     String,
                         @ApiModelProperty(value="requester id", example="7bff1d74-e3f0-4188-8acb-905f06705e43") requesterId:   String,
                         @ApiModelProperty(value="VDC Blueprint ID") blueprintId: String)


