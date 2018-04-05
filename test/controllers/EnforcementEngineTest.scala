import javax.inject.Inject

import controllers.EnforcementEngine
import models.{QueryObject, ResponseQuery}
import play.api.i18n.Messages
import play.api.mvc._
import play.api.test._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.libs.json.Json

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class EnforcementEngineTest extends PlaySpecification with Results  {

  "User" should {
    "have a name" in {
      val user = "Player"
      user must beEqualTo("Player")
    }
  }

  private def controller = {
    new EnforcementEngine() {
      override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
    }
  }


  "Query Rewrite" should {
    "be valid" in {
      val query = "query"
      val queryObject = new QueryObject(query, "purpose", "requester", "blueprint")
      val request: FakeRequest[QueryObject] =  FakeRequest().withBody(queryObject)
      val result: Future[Result] = controller.rewriteSQLQuery()(request)
      status(result) must equalTo(OK)
      contentType(result) must equalTo(Some("application/json"))
      val expectedJson = Json.toJson(ResponseQuery(query))
      contentAsJson(result) must equalTo(expectedJson)

    }
  }
}