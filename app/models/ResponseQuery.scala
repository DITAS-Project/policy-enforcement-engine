package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}

object ResponseQuery {
  implicit val responseQueryWrites: Writes[ResponseQuery] = Json.writes[ResponseQuery]
  implicit val responseQueryReads: Reads[ResponseQuery] = Json.reads[ResponseQuery]
}


case class ResponseQuery(
                          @ApiModelProperty(value="json containing the result", example="[{patientId:1,cholesterol:242.0,wbc:7610]") result:       String)

