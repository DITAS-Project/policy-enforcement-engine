pipeline {
    agent none

    stages {

        stage('Build') {
           agent {
                dockerfile {
                    filename 'Dockerfile.build'
		    //args '-v /home/cloudsigma/jencache/.sbt:/root/.sbt -v /home/cloudsigma/jencache/.ivy2:/root/.ivy2'
                 }
           }
            steps {
                echo "Compiling..."
                //sh "sbt -Dsbt.global.base=/root/.sbt -Dsbt.boot.directory=/root/.sbt -Dsbt.ivy.home=/root/.ivy2 assembly"
		//sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 assembly"
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 universal:packageZipTarball"
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 test"
                sh "tar -xvf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz -C target/universal/"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT/lib/org.glassfish.jersey.bundles.repackaged.jersey-guava-2.22.2.jar"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz"
                sh "tar -cvzf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz target/universal/policy-enforcement-engine-1.0-SNAPSHOT"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT"
                sh "ls target/universal/"
                sh "tar -xvf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz -C target/universal/"
                sh "ls target/universal/policy-enforcement-engine-1.0-SNAPSHOT/lib/org.glassfish.jersey.bundles.repackaged.jersey-guava-2.22.2.jar"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                //echo "Archiving artifacts..."
                //archiveArtifacts 'target/target/universal/policy-enforcement-engine-1.0-SNAPSHOT/lib/*.jar'
                echo "Done."

                // Run the tests (we don't use a different stage for improving the performance, another stage would mean another agent)
		//sh "sbt -Dsbt.global.base=/root/.sbt -Dsbt.boot.directory=/root/.sbt -Dsbt.ivy.home=/root/.ivy2 test"
		//sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 test"
            }

            post {
                always {
                    // Record the jUnit test
                    junit 'target/test-reports/*.xml'
                }
            }
        }


    }
}
