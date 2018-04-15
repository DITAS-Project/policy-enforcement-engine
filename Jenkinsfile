pipeline {
    agent none

    stages {

        stage('Build') {
           agent {
                dockerfile {
                    filename 'Dockerfile.build'
                  //  args '-u 1000 -v /home/cloudsigma/jencache:/tmp'
                 }
           }
            steps {
                echo "Compiling..."
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 assembly"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                echo "Archiving artifacts..."
                archiveArtifacts 'target/scala-2.12/*.jar'
                echo "Done."

                // Run the tests (we don't use a different stage for improving the performance, another stage would mean another agent)
		sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 test"
            }

            post {
                always {
                    // Record the jUnit test
                    junit 'target/test-reports/*.xml'
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
                archiveArtifacts 'Dockerfile'
                sh "which docker"
                // This will search for a Dockerfile in the src folder and will build the image to the local repository
                // Using latest tag to override tha newest image in the hub
                sh "docker build -t \"ditas/policy-enforcement-engine:latest\" ."
                echo "Done"
            }
        }
        stage('Push image') {
            agent any
            options {
                // Already compiled the WAR, so don't checkout againg (checkout also cleans the workspace, removing any generated artifact)
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

        stage('Docker Image Creation') {         
            agent {
	         dockerfile {
	           filename 'Dockerfile.build'
	         }
	    }
            steps {
                // Generate Jenkinsfile and prepare the artifact files.
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 docker"
		echo 'Building and pushing'
		// sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 dockerBuildAndPush"
		    
		// The following code is not necessary as "dockerBuildAndPush" should do all the work 
		/*
                echo 'Creating the image...'
                // This will search for the Dockerfile in the target directory and build the image to the local repository
                sh "docker build -t \"ditas/policy-enforcement-engine:latest\" -f target/docker/Dockerfile ."
                echo "Done"
                echo 'Retrieving Docker Hub password from /opt/ditas-docker-hub.passwd...'
                // Get the password from a file. This reads the file from the host, not the container. Slaves already have the password in there.
                script {
                    password = readFile '/opt/ditas-docker-hub.passwd'
                }
                echo "Done"
                echo 'Login to Docker Hub as ditasgeneric...'
                sh "docker login -u ditasgeneric -p ${password}"
                echo "Done"
                echo "Pushing the image ditas/policy-enforcement-engine:latest..."
                sh "docker push ditas/policy-enforcement-engine:latest"
		*/
                echo "Done(FIN)"
            }
       }        
    }
}
