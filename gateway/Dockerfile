FROM openjdk:17-alpine
MAINTAINER InnLab KZ

ADD ./target/gateway-0.0.1-SNAPSHOT.jar /app/
ADD ./source /app/source
CMD ["java", "-Xmx200m", "-jar", "/app/gateway-0.0.1-SNAPSHOT.jar", "--spring.config.name=bootstrap", "--spring.config.location=file:/app/source/"]

EXPOSE 4000
