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


