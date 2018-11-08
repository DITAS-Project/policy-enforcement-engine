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

object TableName {
  implicit val responseQueryWrites: Writes[TableName] = Json.writes[TableName]
  implicit val responseQueryReads: Reads[TableName] = Json.reads[TableName]
}

case class TableName(@ApiModelProperty(value="The name of the table",
  dataType="String",
  example="blood_tests")
                     name:       String)