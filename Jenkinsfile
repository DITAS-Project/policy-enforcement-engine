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
                COPY .preprocessing-spark-1.0-SNAPSHOT-jar-with-dependencies.jar lib/
                COPY .demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar lib/
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 universal:packageZipTarball"
                sh "sbt -Dsbt.global.base=.sbt -Dsbt.boot.directory=.sbt -Dsbt.ivy.home=.ivy2 test"
                sh "rm -rf lib/*.jar"
                sh "ls target/universal/"
                //de-compress the tgz
                sh "tar -xvf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz -C target/universal/"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT/lib/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT/lib/preprocessing-spark-1.0-SNAPSHOT-jar-with-dependencies.jar"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz"
                //compress without the jars
                sh "tar -cvzf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz target/universal/policy-enforcement-engine-1.0-SNAPSHOT"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT"
                //make final checks
                sh "tar -xvf target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz -C target/universal/"
                sh "ls target/universal/policy-enforcement-engine-1.0-SNAPSHOT/preprocessing-spark-1.0-SNAPSHOT-jar-with-dependencies.jar"
                sh "ls target/universal/policy-enforcement-engine-1.0-SNAPSHOT/demo-sparksql-1.0-SNAPSHOT-jar-with-dependencies.jar"
                sh "rm -rf target/universal/policy-enforcement-engine-1.0-SNAPSHOT"
                sh "ls"
                echo "Done."
		    
                // Lets make the JAR available from the artifacts tab in Jenkins
		    
                //echo "Archiving artifacts..."
                //archiveArtifacts 'target/universal/policy-enforcement-engine-1.0-SNAPSHOT.tgz'
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
