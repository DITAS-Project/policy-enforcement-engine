
import scala.concurrent.Future

import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import controllers.EnforcementEngine
import org.scalatest.mock.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import play.api.i18n.MessagesApi
import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import play.api.Application
import play.test.WithApplication
import play.api.inject.guice.GuiceApplicationBuilder
import controllers.EnforcementEngine
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import models.QueryObject
import play.api.libs.json.Json
import akka.stream.Materializer



@RunWith(classOf[JUnitRunner])
class EnforcementEngineTest extends PlaySpec with ScalaFutures with MockitoSugar {

//    val injector = new GuiceInjectorBuilder()
//    .overrides(bind[MessagesApi].toInstance(messagesApi))
//    .overrides(bind[ExecutionContext].toInstance(ec))
//    .overrides(bind[ActorSystem].toInstance(actorSystem))
//    .injector
//  

//  "EnforcementEngineTest" should {
//    "should be valid" in {
//      val controller = new EnforcementEngine
//      val result: Future[Result] = controller.rewriteSQLQuery()
//      val bodyText: String = contentAsString(result)
//      bodyText mustBe "query"
//      
//      
//    }
    
    "respond to the index Action" in new WithApplication {
    val app: Application = GuiceApplicationBuilder().build()
    val messagesApi = app.injector.instanceOf[MessagesApi]
    val ec = app.injector.instanceOf[ExecutionContext]
    val mat = app.injector.instanceOf[Materializer]
    val controller = new EnforcementEngine(Helpers.stubControllerComponents(BodyParser.Default, messagesApi))      
      val queryObject = new QueryObject("query", "purpose", "requester", "blueprint")
              
    val request = FakeRequest().withJsonBody(Json.toJson(queryObject))
  val result = controller.rewriteSQLQuery(request)
  val expectedJson = Json.parse("query")
  status(result) must equalTo(OK)
    contentType(result) must equalTo(Some("application/json"))
    contentAsJson(result) must equalTo(expectedJson)
  
  

//  contentType(result) must beSome("text/plain")
//  contentAsString(result) must contain("Hello Bob")
}


  
}