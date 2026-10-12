# STAGE 1: Builder (Heavy JDK + Maven)
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace

# Install maven into the Alpine builder
RUN apk add --no-cache maven

# Copy configuration and source code
COPY pom.xml .
COPY src src

# Build the jar using maven directly
RUN mvn clean package -DskipTests -B

# STAGE 2: Hardened Production Runtime (Lightweight JRE only)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S wattwise && adduser -S wattwise -G wattwise
USER wattwise:wattwise

COPY --from=builder /workspace/target/wattwise-*.jar app.jar
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC"

HEALTHCHECK --interval=20s --timeout=4s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]