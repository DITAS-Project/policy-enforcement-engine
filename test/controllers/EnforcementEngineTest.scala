import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.mvc._
import play.api.test._


import javax.inject.Inject
import scala.concurrent.Future

import models.QueryObject





@RunWith(classOf[JUnitRunner])
class EnforcementEngineTest extends PlaySpecification with Results {

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
    
  "Application" should {
  "be reachable" in new WithServer {
    val response = await(WS.url("http://localhost:9000/docs").get()) //1

    response.status must equalTo(OK) //2
    response.body must contain("Semaphore Community Library") //3
  }
}
  
  "Example Page#index" should {
    "be valid" in {
      val controller = new EnforcementEngine(Helpers.stubControllerComponents())
      val result: Future[Result] = controller.index().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must be equalTo "ok"
    }
  }  
  
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