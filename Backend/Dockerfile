FROM eclipse-temurin:21-jre-jammy

# Update vulnerable Ubuntu packages
RUN apt-get update \
    && apt-get install --only-upgrade -y apt libapt-pkg6.0 \
    && rm -rf /var/lib/apt/lists/*


WORKDIR /app

RUN useradd --system --create-home appuser

COPY target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]