FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S katm && adduser -S katm -G katm

COPY target/katm-notification-1.0.0-SNAPSHOT.jar app.jar

RUN chown katm:katm app.jar
USER katm

EXPOSE 8082

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1
