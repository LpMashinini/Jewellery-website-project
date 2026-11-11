FROM eclipse-temurin:21-jre-jammy

# Update APT and install the security-patched packages
RUN apt-get update -o Acquire::Retries=0 \
    && apt-get install --only-upgrade -y \
       -o Acquire::Retries=0 \
       apt=2.4.15ubuntu0.1 \
       libapt-pkg6.0=2.4.15ubuntu0.1 \
    && dpkg-query -W -f='${Version}\n' apt \
    && dpkg-query -W -f='${Version}\n' libapt-pkg6.0 \
    && test "$(dpkg-query -W -f='${Version}' apt)" = "2.4.15ubuntu0.1" \
    && test "$(dpkg-query -W -f='${Version}' libapt-pkg6.0)" = "2.4.15ubuntu0.1" \
    && rm -rf /var/lib/apt/lists/*


WORKDIR /app

RUN useradd --system --create-home appuser

COPY target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]