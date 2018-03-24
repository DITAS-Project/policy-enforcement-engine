package models


import play.api.libs.json.Json

case class QueryObject(query: String,
    purpose: String,
    requester: String,
    blueprintId: String) {

}

object QueryObject {
  implicit val queryObjectFormat = Json.format[QueryObject]

}

