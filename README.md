## License 
Copyright 2018 IBM

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.

This is being developed for the DITAS Project: https://www.ditas-project.eu/

## policy-enforcement-engine

Policy enforcement engine enforces data access policies when VDC accesses its data sources. 
policy-enforcement-engine-api is a Play Framework application, which uses an enforcement engine that implements policy-enforcement-engine-interface.

## Description 

Returns a rewritten query and a set of corresponding data tables to retrieve the compliant data.

## Functionalities

`POST` `/rewrite-sql-query`

  * **description**: Returns a rewritten query and a set of corresponding data tables to
    retrieve the compliant data. 
  * **caller** eHealth VDC
  * **input**: Application requirements JSON file which includes the access type, purpose, requesterId and blueprint number
  * **output**: JSON response as described above

## Installation
Clone repository

Create the policy-enforcement-interface with:
```
cd policy-enforcement-engine-interface
mvn clean install
```
Create a lib folder inside policy-enforcement-engine-api, and put there a jar of implementation of the policy-enforcement-engine-interface.
dummy-policy-enforcement-engine/ folder can be used as an example for implementation of the policy-enforcement-engine-interface.

Create distribution with:
```
cd policy-enforcement-engine-api
sbt universal:packageZipTarball
```

Unzip the archive in target/universal/:
```
tar xvfz policy-enforcement-engine-1.0.tgz
```


## Execution:

The conf/reference.conf file provides default configuration parameters; they are overridden by any settings defined in the conf/application.conf file. Please, copy the contents of the file conf/reference.conf to conf/application.conf and replace with the correct values of your runtime.

* Use the following command to run the application.

```
app/policy-enforcement-engine-1.0/bin/policy-enforcement-engine  -Dplay.http.secret.key='your-secret' -Dconfig.file='\/conf\/application.conf'
```

* Example call:
```
curl -X POST "http://<hostname>:9000/rewrite-sql-query" -H "accept: application/json" -H "Content-Type: application/json" --data {"query": "SELECT patientId, date, cholesterol.hdl.value FROM blood_tests", "purpose": "MedicalTreatment", "access": "read", "blueprintId": "2", "requesterId": <RequesterId>}
```

## Documentation:
```
Go to http://<hostname>:9000/docs/swagger.json 
```

For Swagger UI - Go to 
```
http://<hostname>:9000/docs/ 
```
and paste the url path to your swagger.json in the text box.

