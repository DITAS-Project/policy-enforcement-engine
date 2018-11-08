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
import javax.inject.Inject
import controllers.EnforcementEngine
import models.{RequestQuery, ResponseQuery}
import play.api.i18n.Messages
import play.api.mvc._
import play.api.test._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.Configuration
import play.api.libs.json.Json
import bootstrap.Init
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class EnforcementEngineTest extends PlaySpecification with Results  {
  val config: Configuration = null
  val initService: Init = null


  private def controller = {
    new EnforcementEngine(null, null) {
      override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }
  }


  "Query Rewrite" should {
    "fail as there is no config file" in {
      val query = "query"
      val queryObject = new RequestQuery(query, "", "", "", "")
      val request: FakeRequest[RequestQuery] =  FakeRequest().withBody(queryObject)
      val result: Future[Result] = controller.rewriteSQLQuery(request)
      status(result) must equalTo(BAD_REQUEST)
    }
  }
}
