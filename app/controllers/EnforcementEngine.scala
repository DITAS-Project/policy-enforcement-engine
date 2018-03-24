package controllers

import scala.concurrent.Future

import org.apache.spark.sql.SparkSession

import bootstrap.Init
import javax.inject._
import javax.inject.Inject
import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.mvc.BodyParser
import play.api.Logger

import models.QueryObject

// TODO thread pool!!!

class EnforcementEngine @Inject() (config: Configuration, initService: Init, parser: BodyParser.Default)(implicit
  webJarAssets: WebJarAssets, val messagesApi: MessagesApi) extends Controller {


  // TODO add request and requester attributes
  def rewriteSQLQuery(query: String, purpose: String, blueprintId: String): Action[AnyContent] = {
    Action.async { 
//      request =>
//      val requestObject = Json.parse[QueryObject](request.body)
//      val query = request.body
      Logger.info(s"Received query: $query, for purpose: $purpose, blueprintId: $blueprintId")
      var newQuery = query
      Future.successful(Ok(Json.toJson(newQuery)))
    }
  }


//  def getTestValues(socialId: String, testType: String): Action[AnyContent] = {
//    Action.async {
//      val spark = initService.getSparkSessionInstance
//      readData(spark)
//      val query = "select patientId, date, %s.value as %s from joined where socialId='%s'".format(testType, testType, socialId)
//      val patientBloodTestsDF = spark.sql(query)
//      patientBloodTestsDF.show(false)
//      patientBloodTestsDF.printSchema
//
//      val rawJson = patientBloodTestsDF.toJSON.collect().mkString
//      Future.successful(Ok(Json.toJson(rawJson)))
//    }
//  }
  
}


