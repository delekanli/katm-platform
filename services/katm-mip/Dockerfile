# ── Stage 1: Build ────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build

COPY pom.xml .
COPY libs/common/pom.xml              libs/common/
COPY services/katm-auth/pom.xml       services/katm-auth/
COPY services/katm-client/pom.xml     services/katm-client/
COPY services/katm-claim/pom.xml      services/katm-claim/
COPY services/katm-contract/pom.xml   services/katm-contract/
COPY services/katm-gateway/pom.xml    services/katm-gateway/
COPY services/katm-mip/pom.xml        services/katm-mip/
COPY services/katm-notification/pom.xml services/katm-notification/
COPY services/katm-report/pom.xml     services/katm-report/
COPY services/katm-scheduler/pom.xml  services/katm-scheduler/

COPY libs/common/src  libs/common/src
COPY services/katm-mip/src services/katm-mip/src

RUN --mount=type=cache,id=katm-maven,target=/root/.m2 \
    mvn -pl libs/common,services/katm-mip -am package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S katm && adduser -S katm -G katm
USER katm
COPY --from=build /build/services/katm-mip/target/katm-mip-*.jar app.jar
EXPOSE 8089
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8089/actuator/health || exit 1
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
