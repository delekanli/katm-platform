# KATM Platform

Enterprise microservices platform for credit bureau and insurance management in Uzbekistan.

## Tech Stack

| Component     | Technology                         |
|---------------|------------------------------------|
| Language      | Java 21                            |
| Framework     | Spring Boot 4.0 + Spring Cloud     |
| Database      | Oracle 19c (JDBC + stored procs)   |
| Auth          | Keycloak 24 + OAuth2 / JWT         |
| Messaging     | Apache Kafka                       |
| Scheduler     | Quartz (JDBC store, Oracle)        |
| Storage       | S3-compatible (MinIO)              |
| Observability | Prometheus + Grafana + Zipkin      |
| API Docs      | SpringDoc OpenAPI 3 (aggregated)   |

## Architecture

```
Internet
    │
    ▼
┌─────────────────────────────────────────────┐
│              katm-gateway  :8080            │
│   Spring Cloud Gateway · OAuth2 JWT guard  │
│   Kubernetes service-discovery (lb://)     │
└──────────────────┬──────────────────────────┘
                   │ routes
       ┌───────────┼──────────────────────────────────┐
       ▼           ▼            ▼           ▼          ▼
  katm-auth   katm-client  katm-claim  katm-contract  katm-report
    :8081       :8087        :8085       :8086          :8084
       │                       │            │
       └───────────────────────┴────────────┘
                       │  Kafka events
                       ▼
              katm-notification  :8088
              (SMS outbox + email + OTP)
                       ▲
                       │  jobs.sms.send
              katm-scheduler  :8091
              (Quartz · every 30 s + cron)
                       │  Kafka trigger
                       ▼
              katm-mip  :8089
              (MIP state system integration)

─────── shared infrastructure ───────────────
  Oracle 19c · Kafka · Keycloak · MinIO
  Prometheus · Grafana · Zipkin
```

## Services

| Service           | Port | Role                                               |
|-------------------|------|----------------------------------------------------|
| katm-gateway      | 8080 | API Gateway, JWT validation, routing, Swagger aggregation |
| katm-auth         | 8081 | Keycloak integration, token management             |
| katm-report       | 8084 | Credit reports (KIB), FICO scoring, Infoscore      |
| katm-claim        | 8085 | Claim management (bank / insurance / retailer)     |
| katm-contract     | 8086 | Contract lifecycle (bank / retailer)               |
| katm-client       | 8087 | Client registry, credit ban status                 |
| katm-notification | 8088 | SMS outbox (PlayMobile), email (SMTP), OTP, subscriptions |
| katm-mip          | 8089 | MIP state system integration                       |
| katm-scheduler    | 8091 | Quartz job scheduler → Kafka triggers              |

## Kafka Topics

| Topic            | Producer         | Consumer             | Description                      |
|------------------|------------------|----------------------|----------------------------------|
| `client.created` | katm-client      | katm-notification    | New client registered            |
| `credit.updated` | katm-report      | katm-notification    | Credit report updated            |
| `report.ready`   | katm-report      | katm-notification    | Report ready for delivery        |
| `jobs.credit.sync` | katm-scheduler | katm-report (future) | Daily credit sync trigger        |
| `jobs.sms.send`  | katm-scheduler   | katm-notification    | SMS outbox flush trigger (30 s)  |

## Scheduled Jobs (Quartz)

| Job            | Schedule          | Action                                        |
|----------------|-------------------|-----------------------------------------------|
| CreditSyncJob  | `0 0 1 * * ?` (01:00 daily) | Publish `TRIGGER` → `jobs.credit.sync` |
| SmsSendJob     | Every 30 seconds  | Publish `TRIGGER` → `jobs.sms.send` → flush SMS outbox from `v_Notif_To_Send` |

### SMS Outbox Flow

```
Quartz SmsSendJob (30 s)
    │
    ▼  Kafka: jobs.sms.send
katm-notification: NotificationKafkaConsumer
    │
    ▼
SmsOutboxService.processOutbox()
    ├── Oracle: SELECT * FROM v_Notif_To_Send
    ├── notif_pkg.set_in_progress(id)
    ├── PlayMobile REST API  (retry ×3, backoff 5 s)
    └── notif_pkg.set_sent(id) / set_failed(id)
```

## Security

JWT tokens issued by Keycloak (`katm` realm). The gateway validates every token before routing; each downstream service re-validates independently.

### Roles

| Role      | Services                                      |
|-----------|-----------------------------------------------|
| ADMIN     | All endpoints                                 |
| BANK      | katm-report, katm-claim (bank), katm-contract (bank), katm-client |
| RETAILER  | katm-claim (retailer), katm-contract (retailer) |
| INSURANCE | katm-claim (insurance)                        |
| CLIENT    | katm-notification (OTP, subscriptions)        |

### JWT Token (example)

```bash
curl -X POST http://localhost:8180/realms/katm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=katm-frontend&username=admin&password=admin"
```

JWT payload includes an `attributes` claim used by all services to resolve `head` and `code`:

```json
{
  "attributes": {
    "head": "002",
    "code": "00450"
  }
}
```

## Quick Start

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### 1. Start Infrastructure

```bash
cp .env .env.local

docker compose up -d oracle-db zookeeper kafka keycloak minio prometheus grafana zipkin

# Wait for Oracle (~2-3 min on first run)
docker compose logs -f oracle-db
```

### 2. Build

```bash
mvn clean package -DskipTests
```

### 3. Start Services

```bash
docker compose up -d

# Or individually for development
mvn -pl services/katm-gateway  spring-boot:run &
mvn -pl services/katm-auth     spring-boot:run &
mvn -pl services/katm-client   spring-boot:run &
```

### 4. Access

| URL | Description |
|-----|-------------|
| http://localhost:8080/swagger-ui.html | Aggregated Swagger UI (all services) |
| http://localhost:8080 | API Gateway |
| http://localhost:8180/admin | Keycloak Admin (admin / admin) |
| http://localhost:3000 | Grafana (admin / admin) |
| http://localhost:9411 | Zipkin distributed tracing |
| http://localhost:9090 | Prometheus |
| http://localhost:9001 | MinIO Console |

## Project Structure

```
katm-platform/
├── libs/
│   └── common/              # Shared: ApiResponse, exceptions, utils
├── services/
│   ├── katm-auth/           # :8081  Auth & Keycloak
│   ├── katm-gateway/        # :8080  API Gateway
│   ├── katm-report/         # :8084  Credit reports & FICO
│   ├── katm-claim/          # :8085  Claims (bank/insurance/retailer)
│   ├── katm-contract/       # :8086  Contracts (bank/retailer)
│   ├── katm-client/         # :8087  Client registry
│   ├── katm-notification/   # :8088  SMS outbox, email, OTP
│   ├── katm-mip/            # :8089  MIP integration
│   └── katm-scheduler/      # :8091  Quartz jobs
├── docker/
│   ├── keycloak/            # Realm export
│   ├── oracle/init/         # DB init scripts
│   ├── prometheus/          # prometheus.yml
│   └── grafana/             # Datasource & dashboard provisioning
├── docker-compose.yml
├── pom.xml                  # Root BOM (Spring Boot 4.0.6)
└── .env                     # Environment variables template
```

## Infrastructure

| Service    | Port | Notes                                  |
|------------|------|----------------------------------------|
| Oracle DB  | 1521 | `jdbc:oracle:thin:@//localhost:1521/KATMDB` |
| Kafka      | 9092 | Single broker (dev)                    |
| Kafka UI   | 8090 | http://localhost:8090                  |
| Keycloak   | 8180 | http://localhost:8180                  |
| MinIO API  | 9000 | S3-compatible storage                  |
| MinIO UI   | 9001 | http://localhost:9001                  |
| Prometheus | 9090 | http://localhost:9090                  |
| Grafana    | 3000 | http://localhost:3000                  |
| Zipkin     | 9411 | http://localhost:9411                  |

## Database Schemas

Each service owns its Oracle schema (Database-per-Service pattern):

| Schema          | Owner service    | Contents                              |
|-----------------|------------------|---------------------------------------|
| `katm_auth`     | katm-auth        | Sessions, token metadata              |
| `katm_client`   | katm-client      | Client registry                       |
| `katm_claim`    | katm-claim       | Claims, claim documents               |
| `katm_contract` | katm-contract    | Contracts, schedules, repayments      |
| `katm_notif`    | katm-notification| SMS outbox (`v_Notif_To_Send`, `notif_pkg`), OTP |
| `katm_report`   | katm-report      | Report cache, FICO results            |
| `katm_scheduler`| katm-scheduler   | Quartz tables (`QRTZ_*`)             |
| `katm_mip`      | katm-mip         | MIP request/response log              |
| `DATAS`         | shared           | Stored procedures called by all services |

## Observability

- **Metrics:** Prometheus scrapes `/actuator/prometheus` — 100 % sampling
- **Tracing:** Zipkin via Micrometer Brave (distributed trace IDs propagated through Kafka headers)
- **Dashboards:** Grafana pre-provisioned with Prometheus datasource
- **Health:** `/actuator/health` on every service (liveness + readiness probes)
