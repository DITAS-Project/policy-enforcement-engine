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
import javax.inject._
import play.api._
import play.api.http.HttpFilters
import play.api.mvc._

import filters.EnforcementHeaderFilter

/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 *
 * Play will automatically use filters from any class called
 * `Filters` that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to
 * the `application.conf` configuration file.
 *
 * @param env Basic environment settings for the current application.
 * @param exampleFilter A demonstration filter that adds a header to
 * each response.
 */
@Singleton
class Filters @Inject() (
  env: Environment,
  exampleFilter: EnforcementHeaderFilter) extends HttpFilters {

  override val filters = {
    // Use the example filter if we're running development mode. If
    // we're running in production or test mode then don't use any
    // filters at all.
    if (env.mode == Mode.Dev) Seq(exampleFilter) else Seq.empty
  }

}
