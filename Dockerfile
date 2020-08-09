FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cdk-app.jar
#ENTRYPOINT ["java","-jar","cdk-app.jar"]
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -jar cdk-app.jar" ]