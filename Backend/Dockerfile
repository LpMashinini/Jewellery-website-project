FROM eclipse-temurin:21-jre-jammy

# Update APT and install the security-patched packages
# Update Ubuntu packages, including the APT security fix
RUN apt-get -o Acquire::Retries=0 update \
    && apt-get -o Acquire::Retries=0 upgrade -y \
    && dpkg-query -W -f='${Version}\n' apt \
    && dpkg-query -W -f='${Version}\n' libapt-pkg6.0 \
    && dpkg --compare-versions "$(dpkg-query -W -f='${Version}' apt)" ge "2.4.15ubuntu0.1" \
    && dpkg --compare-versions "$(dpkg-query -W -f='${Version}' libapt-pkg6.0)" ge "2.4.15ubuntu0.1" \
    && rm -rf /var/lib/apt/lists/*


WORKDIR /app

RUN useradd --system --create-home appuser

COPY target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]