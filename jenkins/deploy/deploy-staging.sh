#!/usr/bin/env bash
# Staging environment: 31.171.247.162
# Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem

# TODO state management? We are killing without careing about any operation the conainer could be doing.

scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/policy-enforcement-engine/connections-DITAS.yml cloudsigma@31.171.247.162:connections-DITAS.yml
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/policy-enforcement-engine/applicationEngine.conf cloudsigma@31.171.247.162:applicationEngine.conf
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/policy-enforcement-engine/demo-dpcm-DITAS.yml cloudsigma@31.171.247.162:demo-dpcm-DITAS.yml
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/policy-enforcement-engine/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar cloudsigma@31.171.247.162:demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar
ssh -i /opt/keypairs/ditas-testbed-keypair.pem cloudsigma@31.171.247.162 << 'ENDSSH'
# Ensure that a previously running instance is stopped (-f stops and removes in a single step)
# || true - "docker stop" failt with exit status 1 if image doen't exists, what makes the Pipeline fail. the "|| true" forces the command to exit with 0.
sudo docker rm -f policy-enforcement-engine || true
sudo docker pull ditas/policy-enforcement-engine:latest
sudo docker run -p 50004:9000 -d  -ti --entrypoint=sh --name policy-enforcement-engine ditas/policy-enforcement-engine:latest
sudo docker rm /app/policy-enforcement-engine-1.0-SNAPSHOT/lib/dummy_demosparksql_2.11-0.1.jar
sudo docker cp applicationEngine.conf  /app/policy-enforcement-engine-1.0-SNAPSHOT/conf/applicationEngine.conf
sudo docker cp connections-DITAS.yml /app/policy-enforcement-engine-1.0-SNAPSHOT/conf/connections-DITAS.yml
sudo docker cp demo-dpcm-DITAS.yml  /app/policy-enforcement-engine-1.0-SNAPSHOT/conf/demo-dpcm-DITAS.yml
sudo docker cp demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar /app/policy-enforcement-engine-1.0-SNAPSHOT/lib/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar
sudo docker exec -d -i policy-enforcement-engine /app/policy-enforcement-engine-1.0-SNAPSHOT/bin/policy-enforcement-engine -Dplay.http.secret.key='wspl4r' -Dconfig.file='app/policy-enforcement-engine/conf/applicationEngine.conf'
ENDSSH
