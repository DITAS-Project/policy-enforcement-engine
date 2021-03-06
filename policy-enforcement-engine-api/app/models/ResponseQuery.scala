/**
 * Copyright 2018 IBM
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package models

import io.swagger.annotations._
import play.api.libs.json.{Json, Reads, Writes}
import play.api.libs.json._
import scala.collection.mutable.ArrayBuffer


object ResponseQuery {
  implicit val responseQueryWrites: Writes[ResponseQuery] = Json.writes[ResponseQuery]
  implicit val responseQueryReads: Reads[ResponseQuery] = Json.reads[ResponseQuery]
}

case class ResponseQuery(@ApiModelProperty(value="The rewritten query", dataType="String",
                         example="SELECT cholesterol from blood_tests WITH blood_tests AS...")
                         rewrittenQuery:       String,
                         @ApiModelProperty(value="The list of tables needed to extract the compliant result")
                         tables:       ArrayBuffer[TableName],
                         @ApiModelProperty(value="A map of encryption properties, if mandated by policies", allowEmptyValue = true)
                         encryptionProperties: ArrayBuffer[EncryptionProperty])



