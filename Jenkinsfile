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
                //First, install policy-enforcement-interface"
                sh "cd policy-enforcement-engine-interface && mvn -Dmaven.repo.local=../.m2 clean install "
                sh "mkdir policy-enforcement-engine-api/lib/"

                //Create the dummy-policy-enforcement-engine jar file"              
                sh "cd dummy-policy-enforcement-engine && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 clean package"
                sh "cp dummy-policy-enforcement-engine/target/scala-2.12/dummy-policy-enforcement-engine_2.12-1.0.jar policy-enforcement-engine-api/lib/dummy_enforcement_engine.jar"
                
                //sh "sbt -Dsbt.global.base=/root/.sbt -Dsbt.boot.directory=/root/.sbt -Dsbt.ivy.home=/root/.ivy2 assembly"
                sh "cd policy-enforcement-engine-api && sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 universal:packageZipTarball"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                echo "Archiving artifacts..."
                //archiveArtifacts 'policy-enforcement-engine-api/target/universal/*.tgz'
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


         stage('Image creation') {
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
                // Using latest tag to override tha newest image in the hub
                sh "docker build -t \"ditas/policy-enforcement-engine:latest\" -f policy-enforcement-engine-api/Dockerfile.artifact policy-enforcement-engine-api"
                echo "Done"
            }
        }
        stage('Push image') {
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
                echo "Pushing the image ditas/policy-enforcement-engine:latest..."
                // Push the image to DockerHub
                sh "docker push ditas/policy-enforcement-engine:latest"
                echo "Done"
            }
        }
	stage('Image deploy') {
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
    }
}
