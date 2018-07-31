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

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class EnforcementEngineTest extends PlaySpecification with Results  {
  val config: Configuration = null


  private def controller = {
    new EnforcementEngine(config) {
      override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }
  }


  "Query Rewrite" should {
    "be valid" in {
      val query = "query"
      val queryObject = new RequestQuery(query, "", "", "", "")
      val request: FakeRequest[RequestQuery] =  FakeRequest().withBody(queryObject)
      val result: Future[Result] = controller.getCompliantDataResult(request)
      status(result) must equalTo(NOT_FOUND)
      //contentType(result) must equalTo(Some("application/json"))
      //val expectedJson = Json.toJson(ResponseQuery(query))
      //contentAsJson(result) must equalTo(expectedJson)

    }
  }
}