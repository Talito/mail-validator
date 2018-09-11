FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/service.jar
VOLUME /tmp
ADD ${JAR_FILE} service.jar
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /service.jar
