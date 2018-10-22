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

# policy-enforcement-engine
Policy enforcement engine enforces data access policies when VDC accesses its data sources. 

Description:
```
Returns a rewritten query and a set of corresponding data tables to 
```

Create distribution with:
```
sbt universal:packageZipTarball
```


Unzip the archive in target/universal/:
```
tar xvfz policy-enforcement-engine-1.0-SNAPSHOT.tgz
```

Run the application:
```
app/policy-enforcement-engine-1.0-SNAPSHOT/bin/policy-enforcement-engine  -Dplay.http.secret.key='your-secret' -Dconfig.file='\/conf\/application.conf'
```

Please note: conf/reference.conf file includes default settings for the configuration file.

Example call:
```
curl -X POST "http://<hostname>:9000/rewrite-sql-query" -H "accept: application/json" -H "Content-Type: application/json" --data {"query": "SELECT patientId, date, cholesterol.hdl.value FROM blood_tests", "purpose": "MedicalTreatment", "access": "read", "blueprintId": "2", "requesterId": "7bff1d74-e3f0-4188-8acb-905f06705e43"}
```

See documentation at http://<hostname>:9000/docs/#/Enforcement\_Engine/rewriteSQLQuery
