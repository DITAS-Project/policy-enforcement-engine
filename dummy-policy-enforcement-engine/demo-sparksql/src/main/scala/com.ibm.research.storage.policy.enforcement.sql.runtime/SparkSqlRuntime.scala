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
