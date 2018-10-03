#!/usr/bin/env bash
# Staging environment: 31.171.247.162
# Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem

# TODO state management? We are killing without careing about any operation the conainer could be doing.

ssh -i /opt/keypairs/ditas-testbed-keypair.pem cloudsigma@31.171.247.162 << 'ENDSSH'
# Ensure that a previously running instance is stopped (-f stops and removes in a single step)
# || true - "docker stop" failt with exit status 1 if image doen't exists, what makes the Pipeline fail. the "|| true" forces the command to exit with 0.
sudo docker rm -f policy-enforcement-engine || true
sudo docker pull ditas/policy-enforcement-engine:latest
sudo docker create --name  policy-enforcement-engine  -p 50004:9000 ditas/policy-enforcement-engine:latest
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/applicationEngine.conf policy-enforcement-engine:/app/policy-enforcement-engine-1.0-SNAPSHOT/conf/application.conf
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/connections-DITAS.yml policy-enforcement-engine:/app/policy-enforcement-engine-1.0-SNAPSHOT/conf/connections-DITAS.yml
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/demo-dpcm-DITAS.yml policy-enforcement-engine:/app/policy-enforcement-engine-1.0-SNAPSHOT/conf/demo-dpcm-DITAS.yml
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar policy-enforcement-engine:/app/policy-enforcement-engine-1.0-SNAPSHOT/lib/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar
sudo docker exec policy-enforcement-engine rm -rf /app/policy-enforcement-engine-1.0-SNAPSHOT/lib/dummy_demosparksql_2.11-0.1.jar
sudo docker start policy-enforcement-engine
ENDSSH
