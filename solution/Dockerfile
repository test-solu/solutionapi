FROM openjdk:8-jdk-alpine
ADD target/solutionapi-0.0.1-SNAPSHOT.jar app.jar
ADD src/main/webapp/WEB-INF/jsp/dnk/test.jsp src/main/webapp/WEB-INF/jsp/dnk/test.jsp
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]
