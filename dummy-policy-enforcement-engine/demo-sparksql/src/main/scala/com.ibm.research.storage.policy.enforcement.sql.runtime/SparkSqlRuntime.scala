package com.ibm.research.storage.policy.enforcement.sql.runtime

import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession, _}

import scala.collection.mutable.ArrayBuffer

object SparkSqlRuntime {
  trait RewrittenQueryResponse {
    var rewrittenSQLquery: String = new String
    //Array of table names that the rewrittenSQLquery will be applied on.
    val tableArray: ArrayBuffer[String] = new ArrayBuffer[String]
  }
  object rewrittenQueryResponse extends SparkSqlRuntime.RewrittenQueryResponse

  def getNewQuery(spark: SparkSession, configPath: String, purpose: String, access: String, query: String, requesterProperty: String,
                  requesterVal: String): RewrittenQueryResponse = {
    null
  }



}
