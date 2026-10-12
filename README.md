# WattWise: Smart Energy Consumption & Waste Detection Platform

WattWise is an enterprise-grade Java & Spring Boot microservice designed to track electricity consumption across campus laboratories, office spaces, and commercial buildings. It detects overnight energy leaks, calculates potential cost savings, and exposes real-time telemetry metrics to Prometheus & Grafana.

---

## 🚀 Key Features
- **Energy Tracking:** Ingest and aggregate meter readings (kWh, Active Power, Voltage, Current).
- **Anomaly & Waste Engine:** Flags overnight computer lab leaks (>1500W off-hours), weekend idle power drains, and ratio surges.
- **Cost & Savings Analytics:** Dynamically computes utility electricity bills and savings estimates based on customizable tariffs.
- **Prometheus Telemetry:** Exposes custom business counters (`wattwise_energy_recorded_kwh_total`, `wattwise_anomalies_detected_total`, `wattwise_estimated_waste_kwh_total`) via Spring Actuator.

---

## 🛠️ Tech Stack
- **Backend:** Java 25 LTS, Spring Boot 4.x (Web, JPA, Validation, Actuator)
- **Database:** PostgreSQL 16 (Flyway database migrations)
- **Monitoring:** Micrometer, Prometheus, Grafana
- **Containers:** Multi-stage Dockerfile, Docker Compose
- **DevOps:** Jenkins CI, SonarQube, Trivy, Argo CD (GitOps), Kubernetes

---

## 🧪 Local Execution

### 1. Build & Run Tests
```bash
mvn clean verify
```

### 2. Run Local Multi-Container Stack (App + PostgreSQL)
```bash
docker compose up --build -d
```

### 3. Verify Health
```bash
curl http://localhost:8080/actuator/health
```

### 4. Metrics Endpoint for Prometheus
```bash
curl http://localhost:8080/actuator/prometheus
```
