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
import java.util

import io.swagger.core.filter.SwaggerSpecFilter
import io.swagger.model.ApiDescription
import io.swagger.models.{Model, Operation}
import io.swagger.models.parameters.Parameter
import io.swagger.models.properties.Property

class SwaggerConfigurationFilter extends SwaggerSpecFilter {

  override def isParamAllowed(
                               parameter: Parameter,
                               operation: Operation,
                               api: ApiDescription,
                               params: util.Map[String, util.List[String]],
                               cookies: util.Map[String, String],
                               headers: util.Map[String, util.List[String]]
                             ): Boolean = true

  override def isPropertyAllowed(
                                  model: Model,
                                  property: Property,
                                  propertyName: String,
                                  params: util.Map[String, util.List[String]],
                                  cookies: util.Map[String, String],
                                  headers: util.Map[String, util.List[String]]
                                ): Boolean = if (property.getName == "id") false else true

  override def isOperationAllowed(
                                   operation: Operation,
                                   api: ApiDescription,
                                   params: util.Map[String, util.List[String]],
                                   cookies: util.Map[String, String],
                                   headers: util.Map[String, util.List[String]]
                                 ): Boolean = true
}

