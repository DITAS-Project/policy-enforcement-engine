pipeline {
    agent none

    stages {

        stage('Build') {
           agent {
                dockerfile {
                    filename 'policy-enforcement-engine-api/Dockerfile.build'
		    //args '-v /home/cloudsigma/jencache/.sbt:/root/.sbt -v /home/cloudsigma/jencache/.ivy2:/root/.ivy2'
                 }
           }
            steps {
                echo "Compiling..."
                //First, create the policy-enforcement-interface jar file"
                sh "cd policy-enforcement-engine-interface && mvn clean && mvn compile && mvn package"
                //copy the jar
                sh "mkdir policy-enforcement-engine-api/lib/"
                sh "mkdir dummy-policy-enforcement-engine/lib/"
                sh "cp policy-enforcement-engine-interface/target/policy-enforcement-engine-interface-1.0.jar dummy-policy-enforcement-engine/lib/"
                sh "cp policy-enforcement-engine-interface/target/policy-enforcement-engine-interface-1.0.jar policy-enforcement-engine-api/lib/" 

                //Create the dummy-policy-enforcement-engine jar file"              
                sh "cd dummy-policy-enforcement-engine && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 compile && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 package"
                sh "cp dummy-policy-enforcement-engine/target/scala-2.11/dummy-policy-enforcement-engine_2.11-1.0.jar policy-enforcement-engine-api/lib/dummy_enforcement_engine.jar"
                
                //sh "sbt -Dsbt.global.base=/root/.sbt -Dsbt.boot.directory=/root/.sbt -Dsbt.ivy.home=/root/.ivy2 assembly"
                sh "cd policy-enforcement-engine-api && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 universal:packageZipTarball"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                echo "Archiving artifacts..."
                archiveArtifacts 'policy-enforcement-engine-api/target/universal/*.tgz'
                echo "Done."

                // Run the tests (we don't use a different stage for improving the performance, another stage would mean another agent)
		//sh "sbt -Dsbt.global.base=/root/.sbt -Dsbt.boot.directory=/root/.sbt -Dsbt.ivy.home=/root/.ivy2 test"
		sh "cd policy-enforcement-engine-api && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 test"
            }

            post {
                always {
                    // Record the jUnit test
                    junit 'policy-enforcement-engine-api/target/test-reports/*.xml'
                }
            }
        }


         stage('Staging image creation') {
            agent any
            options {
                // Already compiled the WAR, so don't checkout againg (checkout also cleans the workspace, removing any generated artifact)
                skipDefaultCheckout true
            } 
            steps {
                echo 'Creating the image...'
                archiveArtifacts 'policy-enforcement-engine-api/Dockerfile.artifact'
                sh "which docker"
                // This will search for a Dockerfile in the src folder and will build the image to the local repository
                sh "docker build -t \"ditas/policy-enforcement-engine:staging\" -f policy-enforcement-engine-api/Dockerfile.artifact policy-enforcement-engine-api"
                echo "Done"
            }
        }
     stage('Push staging image') {
            agent any
            options {
                // Already compiled the assembly, so don't checkout againg (checkout also cleans the workspace, removing any generated artifact)
                skipDefaultCheckout true
            }
            steps {
                echo 'Retrieving Docker Hub password from /opt/ditas-docker-hub.passwd...'
                // Get the password from a file. This reads the file from the host, not the container. Slaves already have the password in there.
                script {
                    password = readFile '/opt/ditas-docker-hub.passwd'
                }
                echo "Done"
                // Login to DockerHub with the ditas generic Docker Hub user
                echo 'Login to Docker Hub as ditasgeneric...'
                sh "docker login -u ditasgeneric -p ${password}"
                echo "Done"
                echo "Pushing the image ditas/policy-enforcement-engine:staging..."
                // Push the image to DockerHub
                sh "docker push ditas/policy-enforcement-engine:staging"
                echo "Done"
            }
        }
	stage('Image deployment in staging') {
	    agent any
            options {
                // skip checking out code again 
                skipDefaultCheckout true
            }
	    steps {
	       // Staging environment: 31.171.247.162
    	       // Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem
	 	// Call the deployment script
		echo "Deploying..."
		sh 'policy-enforcement-engine-api/jenkins/deploy/deploy-staging.sh'
		echo "Deploy done!"
	   }
	}
        stage('Production image creation') {
            agent any
            steps {
                // Change the tag from staging to production 
                sh "docker tag ditas/policy-enforcement-engine:staging ditas/policy-enforcement-engine:production"
                sh "docker push ditas/policy-enforcement-engine:production"
            }
        }
        stage('Deployment in Production') {
            agent any
            steps {
                sh 'policy-enforcement-engine-api/jenkins/deploy/deploy-production.sh'
            }
        }
    }
}
