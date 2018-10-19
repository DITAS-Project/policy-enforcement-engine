package com.ibm.research.storage.policy.enforcement.sql.runtime

import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession, _}

object SparkSqlRuntime {
  def getCompliantResult(configPath: String, purpose: String, access: String, query: String, requesterProperty: String,
                         requesterVal: String): String = {

    "hello 2"
  }

  def getNewQuery(spark: SparkSession, configPath: String, purpose: String, access: String, query: String, requesterProperty: String,
                  requesterVal: String): String = {
    "hello 1"
  }

  def main(args: Array[String]): Unit = {
    getNewQuery(null, "", "", "", "", "", "")
    getCompliantResult("","","","","","")
  }


}