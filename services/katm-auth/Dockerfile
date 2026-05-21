FROM eclipse-temurin:21-jre-alpine AS base
WORKDIR /app

FROM base AS builder
COPY target/katm-auth-1.0.0-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM base
RUN addgroup -S katm && adduser -S katm -G katm
USER katm

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

EXPOSE 8081

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
