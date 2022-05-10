FROM openjdk:17-alpine
MAINTAINER InnLab KZ

ADD ./target/config-0.0.1-SNAPSHOT.jar /app/
ADD ./source /app/source
CMD ["java", "-Xmx200m", "-jar", "/app/config-0.0.1-SNAPSHOT.jar", "--spring.config.name=application", "--spring.config.location=file:/app/source/"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888
