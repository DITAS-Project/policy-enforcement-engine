pipeline {
    agent none

    stages {

        stage('Build') {
           agent {
                dockerfile {
                    filename 'Dockerfile.build'
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

        stage('Docker Image Creation') {         
            agent {
	         dockerfile {
	           filename 'Dockerfile.build'
	         }
	    }
            steps {
                // Generate Jenkinsfile and prepare the artifact files.
                // sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 docker"
		echo 'Building and pushing'
		sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 dockerBuildAndPush"
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
