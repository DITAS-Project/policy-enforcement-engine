package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.json._
import scala.collection.mutable.ArrayBuffer


object ResponseQuery {
  implicit val responseQueryWrites: Writes[ResponseQuery] = Json.writes[ResponseQuery]
  implicit val responseQueryReads: Reads[ResponseQuery] = Json.reads[ResponseQuery]
}

case class ResponseQuery(@ApiModelProperty(value="The rewritten query", dataType="String", example="WITH blood_tests AS...")
                         rewrittenQuery:       String,
                         @ApiModelProperty(value="The list of tables needed to extract the compliant result")
                         tables:       ArrayBuffer[TableName])



