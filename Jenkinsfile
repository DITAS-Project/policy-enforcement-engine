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
                sh "sbt assembly"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                echo "Archiving artifacts..."
                archiveArtifacts 'target/scala-2.11/*.jar'
                echo "Done."

                // Run the tests (we don't use a different stage for improving the performance, another stage would mean another agent)
		//sh 'sbt test'
            }

            post {
                always {
                    // Record the jUnit test
                    junit 'target/test-reports/*.xml'
                }
            }
        }

        stage('Docker Publish') {
            agent any
            steps {
                // Generate Jenkinsfile and prepare the artifact files.
                sh "sbt docker"

                // Run the Docker tool to build the image
                script {
                    docker.withTool('docker') {
                        docker.build('policy-enforcement-engine:latest', 'target/docker/')
                    }
                }
            }
        }
        stage('Image deploy') {
            // TO-DO avoid downloading the source from git again, not neccessary
            agent any
            steps {
                echo 'to-do'
                // Staging environment: 31.171.247.162
                // Private key for ssh: /opt/keypairs/ditas-testbed-keypair.pem
                // Call the deployment script
                sh './jenkins/scripts/deploy-staging.sh'
            }
        }        
        
        stage('Image creation') {
            agent any
            steps {
                // The Dockerfile.artifact copies the code into the image and run the jar generation.
                echo 'Creating the image...'
                // This will search for a Dockerfile.artifact in the working directory and build the image to the local repository
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
                echo "Done"
            }
       }        
    }
}
