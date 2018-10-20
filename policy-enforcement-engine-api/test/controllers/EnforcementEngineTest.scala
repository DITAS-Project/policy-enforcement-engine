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
