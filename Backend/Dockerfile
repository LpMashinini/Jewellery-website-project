FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Update Ubuntu package lists and upgrade installed packages
RUN apt-get update \
    && apt-get -o Acquire::Retries=0 upgrade -y \
    && echo "Checking APT package versions..." \
    && dpkg-query -W -f='${Package}: ${Version}\n' apt libapt-pkg6.0 \
    && rm -rf /var/lib/apt/lists/*

# Create a non-root application user
RUN useradd --system --create-home --uid 10001 appuser

# Copy the Spring Boot application
COPY --chown=appuser:appuser target/*.jar app.jar

# Run as a non-root user
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]