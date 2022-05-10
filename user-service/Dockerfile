FROM openjdk:17-alpine
MAINTAINER InnLab KZ

ADD ./target/user-service-0.0.1-SNAPSHOT.jar /app/
ADD ./source /app/source
CMD ["java", "-Xmx200m", "-jar", "/app/user-service-0.0.1-SNAPSHOT.jar", "--spring.config.name=bootstrap", "--spring.config.location=file:/app/source/"]

EXPOSE 6008
