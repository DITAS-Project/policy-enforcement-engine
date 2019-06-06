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

object EncryptionProperty {
  implicit val responseQueryWrites: Writes[EncryptionProperty] = Json.writes[EncryptionProperty]
  implicit val responseQueryReads: Reads[EncryptionProperty] = Json.reads[EncryptionProperty]
}


case class EncryptionProperty(@ApiModelProperty(value="Encryption property key-value",
  dataType="String",
  example="a=b")
                              key: String, value: String)