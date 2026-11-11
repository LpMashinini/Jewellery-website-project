FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN apt-get update \
    && apt-cache policy apt libapt-pkg6.0 \
    && apt-get install -y --only-upgrade apt libapt-pkg6.0 \
    && apt-get upgrade -y \
    && dpkg-query -W apt \
    && rm -rf /var/lib/apt/lists/*

RUN useradd --system --create-home --uid 10001 appuser


COPY target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]