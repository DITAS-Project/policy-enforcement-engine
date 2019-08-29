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
package policy.enforcement.engine

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

import scala.collection.mutable

/* Enforcement engine should implement this interface. */
trait PolicyEnforcementEngineInterface {

  def getNewQuery(spark: SparkSession, configPath: String, purpose: String, access: String, query: String,
                  requesterProperty: String, requesterVal: String): RewrittenQueryResponse

  /**
    * Get the required configuration parameters for SparkContext, which enable parquet encryption of datasets
    * in the current session.
    *
    * @param token                      The authentication token to be used when using key manger
    * @param kmsClass                   The class of key protect to use
    * @param keyManagementParametersMap key value for additional parameters.
    * @param policyEngineParametersMap  key value for additional parameters specific to the policy engine implementation.
    *
    */
  //key manager policy enngine paramers keyProtectURL URL
  def getCryptoSessionProperties(token: String, kmsClass: String, kmsInstanceURL: String, policyEngineParametersMap: mutable.Map[String, String]): mutable.Map[String, String]

  /**
    * Get the required configuration parameters for SparkContext, which enable parquet encryption
    * of a specific dataset.
    *
    * @param schema               The schema of the data frame.
    * @param dataSetStoragePath The path to where the data frame will be stored.
    */
  def getDatasetEncryptionProperties(schema: StructType, dataSetStoragePath: String, purpose: String, accessType: String): mutable.Map[String, String]

}
