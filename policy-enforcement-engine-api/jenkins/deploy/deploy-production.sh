#!/usr/bin/env bash
# OSR SDK production environment: 153.92.30.225
# Private key for ssh: /opt/keypairs/osr-sdk-key.pem

# SSH to the OSR and deploy SDK component there
ssh -i /opt/keypairs/osr-sdk-key.pem cloudsigma@153.92.30.225 << 'ENDSSH'
# Ensure that a previously running instance is stopped (-f stops and removes in a single step)
# || true - "docker stop" failt with exit status 1 if image doen't exists, what makes the Pipeline fail. the "|| true" forces the command to exit with 0.
sudo docker stop --time 20 policy-enforcement-engine || true
sudo docker rm -f policy-enforcement-engine || true
sudo docker pull ditas/policy-enforcement-engine:production
sudo docker create --name  policy-enforcement-engine  -p 50004:9000 --restart unless-stopped ditas/policy-enforcement-engine:production 
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/applicationEngine.conf policy-enforcement-engine:/app/policy-enforcement-engine-1.0/conf/application.conf
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/connections-DITAS.yml policy-enforcement-engine:/app/policy-enforcement-engine-1.0/conf/connections-DITAS.yml
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/demo-dpcm-DITAS.yml policy-enforcement-engine:/app/policy-enforcement-engine-1.0/conf/demo-dpcm-DITAS.yml
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/dpcm-config-new.yml policy-enforcement-engine:/app/policy-enforcement-engine-1.0/conf/dpcm-config-new.yml
sudo docker cp /home/cloudsigma/configurations/policy-enforcement-engine/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar policy-enforcement-engine:/app/policy-enforcement-engine-1.0/lib/dummy_enforcement_engine.jar
sudo docker start policy-enforcement-engine
ENDSSH


