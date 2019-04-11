FROM java:8
WORKDIR /app
EXPOSE 8080
COPY /target/BugTracker-1.0-SNAPSHOT.jar /app/BugTracker.jar
ENTRYPOINT ["java","-jar","BugTracker.jar"]