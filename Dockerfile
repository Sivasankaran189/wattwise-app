# Multi-stage Dockerfile for WattWise Smart Energy Platform
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace

# Copy Maven wrapper and dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B || true

# Copy source code and build production jar
COPY src src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Hardened Runtime Container
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Run as non-root user for container security
RUN addgroup -S wattwise && adduser -S wattwise -G wattwise
USER wattwise:wattwise

# Copy compiled JAR from builder stage
COPY --from=builder /workspace/target/wattwise-*.jar app.jar

# JVM memory limits tuned for 8GB development host
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC"
EXPOSE 8080

# Health check using Actuator
HEALTHCHECK --interval=20s --timeout=4s --start-period=30s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
