package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}

object ResponseQuery {
  implicit val responseQueryWrites: Writes[ResponseQuery] = Json.writes[ResponseQuery]
  implicit val responseQueryReads: Reads[ResponseQuery] = Json.reads[ResponseQuery]
}


case class ResponseQuery(
                          @ApiModelProperty(value="json containing the rewritten query and the list of tables needed to extract the compliant result",
                                            example="{\"rewrittenQuery\":\"WITH blood_tests AS\",\"tables\":[{\"name\":\"blood_tests\"},{\"name\":\"blood_tests\"},{\"name\":\"consent\"}]")
                                            result:       String)

