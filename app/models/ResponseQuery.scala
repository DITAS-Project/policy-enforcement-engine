package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}

object ResponseQuery {
  implicit val responseQueryWrites: Writes[ResponseQuery] = Json.writes[ResponseQuery]
  implicit val responseQueryReads: Reads[ResponseQuery] = Json.reads[ResponseQuery]
}


case class ResponseQuery(
                        @ApiModelProperty(value="SQL query", example="SELECT name FROM data WHERE age > 18") query:       String)

