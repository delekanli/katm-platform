# KATM Platform

Микросервисная платформа кредитного бюро Республики Узбекистан (КАТМ) — переписанный
на Spring Boot 4 / Java 21 монолит `katm_new`. Платформа агрегирует кредитные истории,
скоринг, обмен с государственными системами (МИП, E-GOV), реестры участников
(банки, ритейлеры, страховые, лизинговые) и аналитическую отчётность.

---

## 1. Технологический стек

| Компонент          | Технология                                              |
|--------------------|---------------------------------------------------------|
| Язык               | Java 21                                                 |
| Фреймворк          | Spring Boot **4.1.0** + Spring Cloud Gateway (WebFlux)  |
| База данных        | Oracle 19c — `SimpleJdbcCall` (хранимые процедуры) + `JdbcTemplate` / `NamedParameterJdbcTemplate` (pipelined-функции `table(...)`) |
| Аутентификация     | Keycloak 24, OAuth2 Resource Server, JWT                |
| Межсервисный обмен | OAuth2 `client_credentials` (M2M) + REST (`RestClient`) |
| Гос. интеграции    | JAX-WS SOAP (`wsimport`) + Resilience4j (MIP)           |
| Сообщения          | Apache Kafka                                            |
| Планировщик        | Quartz (JDBC-store, Oracle)                             |
| Хранилище файлов   | S3-совместимое (MinIO)                                  |
| Наблюдаемость      | Prometheus + Grafana + Zipkin (Micrometer/Brave)        |
| Документация API   | SpringDoc OpenAPI 3 (агрегируется на шлюзе)             |

---

## 2. Архитектура

```
                          Internet
                             │  (только порт 8080 открыт наружу)
                             ▼
           ┌───────────────────────────────────────────┐
           │            katm-gateway  :8080            │
           │  Spring Cloud Gateway · OAuth2 JWT guard │
           │  Path-маршрутизация · Swagger-агрегация  │
           └───────────────────┬───────────────────────┘
                               │  /api/v1/**  →  lb / http
   ┌──────────┬──────────┬─────┴────┬──────────┬──────────┬──────────┐
   ▼          ▼          ▼          ▼          ▼          ▼          ▼
 auth      client     claim     contract    report   notification  ...
 :8081     :8087      :8085      :8086      :8084      :8088
   │          │          │                    │          ▲
   │          │ M2M      │ M2M                │ M2M      │ Kafka
   │          ▼          ▼                    ▼          │
   │        ucin       mip ◄────── egov ──── ucin       │
   │       :8094      :8089       :8093     :8094        │
   │                  (SOAP к гос.системам)              │
   │                                                     │
   └──────── reference :8090 · registry :8092 ───────────┤
            analytics :8095 · system   :8096            │
                                                         │
        katm-scheduler :8091 ── Kafka: jobs.sms.send ───┘
        (Quartz · 30 c + cron)

──────────────── общая инфраструктура ──────────────────
  Oracle 19c · Kafka · Keycloak · MinIO
  Prometheus · Grafana · Zipkin
```

**Ключевой принцип безопасности:** снаружи доступен **только** шлюз (`:8080`).
Все внутренние сервисы скрыты — в `docker-compose.yml` у них убрана публикация
портов, а в Kubernetes действуют `NetworkPolicy` (см. §8).

---

## 3. Каталог сервисов

| Сервис             | Порт | Базовый путь                    | Назначение |
|--------------------|------|---------------------------------|------------|
| **katm-gateway**   | 8080 | `/**`                           | API-шлюз: валидация JWT, маршрутизация по `Path`, агрегированный Swagger |
| **katm-auth**      | 8081 | `/api/v1/auth`, `/internal`     | Интеграция с Keycloak, управление сотрудниками, сброс пароля, внутренняя проверка доступа |
| **katm-report**    | 8084 | `/api/v1/credits`, `/credits/fcbk` | Кредитные отчёты (КИБ), статус асинхронных отчётов, Infoscore (физ/юр), FICO-скор, FCBK |
| **katm-claim**     | 8085 | `/api/v1/{bank,insurance,retailer}/...` | Заявки на кредитную историю: банк, страховые, ритейлеры/организации (регистрация, ручной ввод, доверенные, отклонение) |
| **katm-contract**  | 8086 | `/api/v1/bank`, `/api/v1/retailer/contract` | Жизненный цикл договоров: банковские и ритейлерские |
| **katm-client**    | 8087 | `/api/v1/clients`               | Реестр клиентов, паспортные данные из МИП, запрет на кредит (установка/снятие/история/по хэшу) |
| **katm-notification** | 8088 | `/api/v1/notifications`      | SMS-outbox (PlayMobile), e-mail (SMTP), OTP, подписки. Kafka-консьюмер |
| **katm-mip**       | 8089 | `/api/v1/mip/...`               | Интеграция с гос. системой МИП по SOAP: граждане, юрлица, должники, налоги, пенсии, GCP, PCD, справочники |
| **katm-reference** | 8090 | `/api/v1/reference`             | Справочники: регионы, районы, статусы/режимы ритейлеров, форматы выгрузки |
| **katm-scheduler** | 8091 | `/api/v1/scheduler`             | Quartz-задания → Kafka-триггеры |
| **katm-registry**  | 8092 | `/api/v1/registry/{insurances,leasings,retailers}` | Реестр участников: регистрация, смена статуса, сброс пароля страховых/лизинговых/ритейлеров |
| **katm-egov**      | 8093 | `/api/v1/egov`                  | Шлюз E-GOV: кредитные отчёты (физ/юр), регистрация заявки (инициализация клиента через UCIN), обновление статуса |
| **katm-ucin**      | 8094 | `/api/v1/ucin/{clients,reports}`| Универсальный кредитный идентификатор: инициализация клиентов (физ/юр), поиск в СИР, тарифы, кредитные отчёты, готовые отчёты по хэшу |
| **katm-analytics** | 8095 | `/api/v1/analytics`             | Аналитика: портфели по банкам/COA, отчёты 04/06/09, дубликаты договоров, статистика отчётов и веб-обращений, скоринг |
| **katm-system**    | 8096 | `/api/v1/system`                | Системные логи шлюза/отчётов, регистрация ошибок онлайн-запросов, статусы повторной отправки в E-GOV |

> Маппинги контроллеров — единственный источник истины; таблица отражает текущее
> состояние `services/*/src/main/java/.../controller`.

---

## 4. Маршрутизация на шлюзе

`katm-gateway` (Spring Cloud Gateway, реактивный) проксирует по префиксу пути.
URI каждого сервиса берётся из переменных окружения с `localhost`-дефолтом:

| Префикс пути              | Сервис             | env-переменная URI       |
|---------------------------|--------------------|--------------------------|
| `/api/v1/auth/**`         | katm-auth          | `KATM_AUTH_URL`          |
| `/api/v1/clients/**`      | katm-client        | `KATM_CLIENT_URL`        |
| `/api/v1/claims/**`       | katm-claim         | `KATM_CLAIM_URL`         |
| `/api/v1/contracts/**`    | katm-contract      | `KATM_CONTRACT_URL`      |
| `/api/v1/reports/**`      | katm-report        | `KATM_REPORT_URL`        |
| `/api/v1/notifications/**`| katm-notification  | `KATM_NOTIFICATION_URL`  |
| `/api/v1/mip/**`          | katm-mip           | `KATM_MIP_URL`           |
| `/api/v1/scheduler/**`    | katm-scheduler     | `KATM_SCHEDULER_URL`     |
| `/api/v1/reference/**`    | katm-reference     | `KATM_REFERENCE_URL`     |
| `/api/v1/registry/**`     | katm-registry      | `KATM_REGISTRY_URL`      |
| `/api/v1/ucin/**`         | katm-ucin          | `KATM_UCIN_URL`          |
| `/api/v1/egov/**`         | katm-egov          | `KATM_EGOV_URL`          |
| `/api/v1/analytics/**`    | katm-analytics     | `KATM_ANALYTICS_URL`     |
| `/api/v1/system/**`       | katm-system        | `KATM_SYSTEM_URL`        |

Каждый сервис дополнительно публикует `GET /v3/api-docs/{service}` — шлюз
переписывает путь (`RewritePath`) и собирает единый Swagger UI на `:8080`.

---

## 5. Безопасность

### 5.1 Пользовательская аутентификация (JWT)

Токены выпускает Keycloak (realm `katm`). Поток:

1. Клиент получает JWT у Keycloak (`password` / `authorization_code`).
2. Шлюз валидирует подпись и `issuer-uri` **до** маршрутизации.
3. Каждый сервис повторно валидирует токен (OAuth2 Resource Server) —
   защита «в глубину», запрос не доверяет только шлюзу.

Роли извлекаются из `realm_access.roles` → `ROLE_*`. Кастомные claim'ы
`head` и `code` (из `attributes` JWT) определяют организацию/филиал и
прокидываются в хранимые процедуры:

```json
{
  "realm_access": { "roles": ["BANK"] },
  "attributes":   { "head": "002", "code": "00450" }
}
```

Получение токена:

```bash
curl -X POST http://localhost:8180/realms/katm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=katm-frontend&username=admin&password=admin"
```

| Роль       | Доступ |
|------------|--------|
| `ADMIN`    | Все эндпоинты |
| `BANK`     | report, claim (bank), contract (bank), client |
| `RETAILER` | claim (retailer), contract (retailer), registry |
| `INSURANCE`| claim (insurance) |
| `CLIENT`   | notification (OTP, подписки), client (запрет/история) |

### 5.2 Межсервисная авторизация (M2M)

Когда сервис вызывает другой сервис (например `katm-egov` / `katm-client` →
`katm-ucin` / `katm-mip`), используется OAuth2 `client_credentials`:

- Конфиденциальный клиент `katm-service` в Keycloak.
- `ServiceTokenProvider` (`libs/common/.../security`) получает токен,
  кэширует его в `AtomicReference<CachedToken>` и обновляет до истечения.
- `authInterceptor()` (`ClientHttpRequestInterceptor`) подставляет
  `Authorization: Bearer …` в исходящие `RestClient`-вызовы.
- Бин регистрируется через автоконфигурацию `KatmHttpAutoConfiguration`.

---

## 6. Доступ к данным (Oracle)

Платформа не использует JPA — вся бизнес-логика живёт в хранимых процедурах и
pipelined-функциях схемы `DATAS` (пакеты `PKG_*`, `NEW_REPORTS_UCIN` и др.).

- **Процедуры с OUT-параметрами** — `SimpleJdbcCall` + `MapSqlParameterSource`.
  Результат-код возвращается в `P_RESULT` / `P_RET_MSG`.
- **Табличные функции** — `NamedParameterJdbcTemplate` +
  `SELECT * FROM table(PKG.fn(...))`.
- **CLOB → текст** — `JdbcUtils.readClob()` читает CLOB как строку и отдаёт
  её UTF-8 байты (`clob.getSubString(1, len).getBytes(UTF_8)`).

### Кредитный отчёт в Base64

`CreditReportResponse.report` имеет тип `byte[]` (UTF-8 текста XML/JSON из CLOB
`P_REPORT`). Jackson по умолчанию сериализует `byte[]` в JSON как **Base64**.
Результат байт-в-байт совпадает с монолитным полем `reportBase64`
(`Base64(reportText.getBytes(UTF-8))`). Двойного кодирования нет — в БД
хранится сырой текст отчёта.

---

## 7. Сообщения и планировщик

### Kafka

| Топик               | Producer        | Consumer            | Назначение                         |
|---------------------|-----------------|---------------------|------------------------------------|
| `jobs.sms.send`     | katm-scheduler  | katm-notification   | Триггер выгрузки SMS-outbox (30 c) |
| `jobs.credit.sync`  | katm-scheduler  | katm-report (план)  | Ежедневная синхронизация кредитов  |

Trace ID пробрасывается через Kafka-заголовки (Micrometer/Brave).

### Quartz (katm-scheduler)

| Задание        | Расписание            | Действие |
|----------------|-----------------------|----------|
| `SmsSendJob`   | каждые 30 секунд      | `TRIGGER` → `jobs.sms.send` → flush outbox `v_Notif_To_Send` |
| `CreditSyncJob`| `0 0 1 * * ?` (01:00) | `TRIGGER` → `jobs.credit.sync` |

**Поток SMS-outbox:**

```
Quartz SmsSendJob (30 c)
   └─ Kafka: jobs.sms.send
        └─ NotificationKafkaConsumer → SmsOutboxService.processOutbox()
              ├─ SELECT * FROM v_Notif_To_Send
              ├─ notif_pkg.set_in_progress(id)
              ├─ PlayMobile REST API   (retry ×3, backoff 5 c)
              └─ notif_pkg.set_sent(id) / set_failed(id)
```

---

## 8. Сетевая изоляция

Наружу открыт только шлюз. Реализовано на двух уровнях:

- **Docker Compose** — у всех `katm-*`, кроме `katm-gateway`, удалена секция
  `ports:` (нет публикации на хост). Сервисы общаются по внутренней сети
  `katm-network`. Проверка: `docker compose config -q`.
- **Kubernetes** — `k8s/network-policy.yaml`, namespace `katm`:
  - `default-deny-ingress` — запрет всего входящего по умолчанию;
  - `allow-intra-namespace` — разрешён трафик pod↔pod внутри namespace;
  - `allow-external-to-gateway` — извне только к `app: katm-gateway`, порт 8080.

---

## 9. Общая библиотека (`libs/common`)

Подключается всеми сервисами; компоненты включаются через
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
(сервисы используют `@SpringBootApplication`, ограниченный пакетом `uz.katm.<svc>`,
поэтому `@Component`/`@Configuration` из common **не** сканируются и должны быть
авто-конфигурацией).

| Пакет       | Содержимое |
|-------------|------------|
| `dto`       | `ApiResponse<T>` — единый конверт ответа |
| `exception` | доменные исключения + `GlobalExceptionHandler` |
| `security`  | `SecurityConfig`, `JwtUtils` (`head`/`code`), `ServiceTokenProvider` (M2M) |
| `config`    | `CommonWebAutoConfiguration`, `KatmHttpAutoConfiguration` |
| `http`      | преднастроенный `RestClient` + M2M-интерсептор |
| `kafka`     | общие конфиги продюсера/консьюмера |
| `util`      | `JdbcUtils` (`readClob`, `toLocalDate`), `PortUtils` |

---

## 10. Быстрый старт

### Требования

- JDK 21
- Maven 3.9+ (либо встроенный в IntelliJ — см. ниже)
- Docker + Docker Compose

> **Сборка без `mvn` в PATH:** проект собирается через Maven из IntelliJ.
> ```powershell
> $env:JAVA_HOME="C:\Program Files\Java\jdk-21"
> & "C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.5\plugins\maven\lib\maven3\bin\mvn.cmd" -am package -DskipTests
> ```
> Флаг `-am` обязателен — `common` не установлен в `.m2`.

### 1. Инфраструктура

```bash
cp .env .env.local        # заполнить креды (Oracle, Keycloak, MinIO, SMS, SMTP, MIP)
docker compose up -d kafka keycloak minio prometheus grafana zipkin
```

> Oracle 19c — внешний (параметры в `.env`: `DB_HOST`/`DB_PORT`/`DB_SERVICE`),
> в `docker-compose.yml` не поднимается.

### 2. Сборка

```bash
mvn clean package -DskipTests
```

### 3. Запуск

```bash
docker compose up -d                       # все сервисы

# либо отдельный сервис в dev-режиме
mvn -pl services/katm-gateway -am spring-boot:run
```

### 4. Точки доступа

| URL                                   | Описание |
|---------------------------------------|----------|
| http://localhost:8080/swagger-ui.html | Агрегированный Swagger UI (все сервисы) |
| http://localhost:8080                 | API-шлюз (единственная внешняя точка) |
| http://localhost:8180/admin           | Keycloak Admin (admin / admin) |
| http://localhost:3000                 | Grafana (admin / admin) |
| http://localhost:9411                 | Zipkin |
| http://localhost:9090                 | Prometheus |
| http://localhost:9001                 | MinIO Console |

---

## 11. Структура проекта

```
katm-platform/
├── libs/
│   └── common/              # ApiResponse, security (JWT+M2M), JdbcUtils, автоконфиги
├── services/
│   ├── katm-gateway/        # :8080  API-шлюз
│   ├── katm-auth/           # :8081  Keycloak, сотрудники
│   ├── katm-report/         # :8084  Кредитные отчёты, FICO, Infoscore, FCBK
│   ├── katm-claim/          # :8085  Заявки (банк/страх/ритейлер)
│   ├── katm-contract/       # :8086  Договоры (банк/ритейлер)
│   ├── katm-client/         # :8087  Реестр клиентов, запрет на кредит
│   ├── katm-notification/   # :8088  SMS-outbox, e-mail, OTP
│   ├── katm-mip/            # :8089  МИП (SOAP + Resilience4j)
│   ├── katm-reference/      # :8090  Справочники
│   ├── katm-scheduler/      # :8091  Quartz → Kafka
│   ├── katm-registry/       # :8092  Реестр участников
│   ├── katm-egov/           # :8093  Шлюз E-GOV
│   ├── katm-ucin/           # :8094  УКИН (клиенты, отчёты)
│   ├── katm-analytics/      # :8095  Аналитика и отчётность
│   └── katm-system/         # :8096  Системные логи, ошибки, статусы E-GOV
├── docker/                  # keycloak realm, oracle init, prometheus, grafana
├── k8s/
│   └── network-policy.yaml  # default-deny + ingress только на шлюз
├── docker-compose.yml       # внешний доступ только у gateway
├── pom.xml                  # корневой BOM (Spring Boot 4.1.0)
└── .env                     # шаблон переменных окружения
```

---

## 12. Инфраструктурные порты

| Сервис      | Порт | Примечание |
|-------------|------|------------|
| Oracle DB   | 1521 | внешний (`jdbc:oracle:thin:@//$DB_HOST:$DB_PORT/$DB_SERVICE`) |
| Kafka       | 9092 | один брокер (dev) |
| Kafka UI    | 8090 | http://localhost:8090 |
| Keycloak    | 8180 | http://localhost:8180 |
| MinIO API   | 9000 | S3-совместимое хранилище |
| MinIO UI    | 9001 | http://localhost:9001 |
| Prometheus  | 9090 | http://localhost:9090 |
| Grafana     | 3000 | http://localhost:3000 |
| Zipkin      | 9411 | http://localhost:9411 |

---

## 13. Наблюдаемость

- **Метрики** — Prometheus скрапит `/actuator/prometheus`.
- **Трейсинг** — Zipkin через Micrometer/Brave; trace ID идёт сквозь Kafka.
- **Дашборды** — Grafana с преднастроенным datasource Prometheus.
- **Health** — `/actuator/health` (liveness + readiness) на каждом сервисе.

---

## 14. Статус миграции

Перенесены все домены монолита `katm_new` (16 модулей реактора). Покрытие
пакетов `PKG_*` — полное; известное исключение — интеграция `humo` (вне объёма).
**Рантайм на живом окружении (Oracle / Keycloak / гос. эндпоинты) не
проверялся** — требуется приёмочный прогон.
