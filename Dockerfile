FROM java:8-jre-alpine
COPY target/scala-2.12/policy-enforcement-engine-assembly-1.0-SNAPSHOT.jar /app/policy-enforcement-engine-assembly-1.0-SNAPSHOT.jar
EXPOSE 9000
ENTRYPOINT ["java", "-Dplay.http.secret.key='wspl4r'", "-jar", "\/app\/policy-enforcement-engine-assembly-1.0-SNAPSHOT.jar"]

