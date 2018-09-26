#!/usr/bin/env bash
# Staging environment: 31.171.247.162
# Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem

# TODO state management? We are killing without careing about any operation the conainer could be doing.

scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/ehealth-sample-spark-vdc/connections-DITAS.yml cloudsigma@31.171.247.162:connections-DITAS.yml
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/ehealth-sample-spark-vdc/applicationEngine.conf cloudsigma@31.171.247.162:applicationEngine.conf
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/ehealth-sample-spark-vdc/demo-dpcm-DITAS.yml cloudsigma@31.171.247.162:demo-dpcm-DITAS.yml
scp -i  /opt/keypairs/ditas-testbed-keypair.pem  /home/cloudsigma/configurations/ehealth-sample-spark-vdc/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar cloudsigma@31.171.247.162:demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar
ssh -i /opt/keypairs/ditas-testbed-keypair.pem cloudsigma@31.171.247.162 << 'ENDSSH'
# Ensure that a previously running instance is stopped (-f stops and removes in a single step)
# || true - "docker stop" failt with exit status 1 if image doen't exists, what makes the Pipeline fail. the "|| true" forces the command to exit with 0.
sudo docker rm -f policy-enforcement-engine || true
sudo docker pull ditas/policy-enforcement-engine:latest
sudo docker run -p 50004:9000 -d  -ti --entrypoint=sh --name policy-enforcement-engine ditas/policy-enforcement-engine:latest
ENDSSH
