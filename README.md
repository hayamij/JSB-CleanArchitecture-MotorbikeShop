# JSB-MotorbikeShop

Full-stack motorbike and accessories commerce system built with Spring Boot and Clean Architecture, with separated domain/business/adapters/infrastructure layers and use-case-oriented flow.

## 1) About project

- Scope: authentication, product catalog (motorbikes + accessories), cart flow, checkout, order tracking, and admin operations.
- Backend: REST APIs implemented through controller -> use case -> repository boundaries.
- Frontend: static pages under `src/main/resources/static` served by Spring MVC.
- Data: SQL Server schema and seed script at project root.

## 2) Tech stack

| Frontend | Backend API | Security | Database | Testing | Tooling |
|---|---|---|---|---|---|
| HTML, CSS, vanilla JavaScript | Java 17, Spring Boot 3.5.6, Spring MVC | Spring Security, CORS config | Microsoft SQL Server, Spring Data JPA/Hibernate | JUnit 5 (`spring-boot-starter-test`) | Maven Wrapper, Spring Boot Maven Plugin, Apache POI |

## 3) Quick setup

- Prerequisites: JDK 17, SQL Server 2019+ (or compatible), Maven (or Maven Wrapper).
- Install/build:

```powershell
./mvnw.cmd clean package -DskipTests
```

- Database reset + seed: run `database-setup.sql` in SSMS. Script recreates database `MotorcycleShop`, creates schema, indexes, triggers, and sample data.
- Application config: adjust datasource settings in `src/main/resources/application.properties`.

```properties
server.port=8080
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MotorcycleShop;encrypt=true;trustServerCertificate=true
spring.datasource.username=fuongtuan
spring.datasource.password=toilabanhmochi
spring.jpa.hibernate.ddl-auto=validate
```

- Run commands:

```powershell
./mvnw.cmd spring-boot:run
./mvnw.cmd test
./mvnw.cmd clean package
```

- Default app URL: `http://localhost:8080`

## 4) Architecture and patterns

- Clean architecture layout:
  - Domain: `src/main/java/com/motorbike/domain`
  - Business: `src/main/java/com/motorbike/business`
  - Adapters: `src/main/java/com/motorbike/adapters`
  - Infrastructure: `src/main/java/com/motorbike/infrastructure`
- Dependency direction: `Infrastructure -> Adapters -> Business -> Domain`.
- Main patterns: use case interactor, repository ports/adapters, presenter + viewmodel mapping, Spring configuration-based dependency wiring.
- Use case composition is applied broadly in `business/usecase/control` for orchestration and reusable sub-flows.

## 5) Source inventory (snapshot)

Snapshot date: 2026-04-08

| Area | Files | Lines | Details |
|---|---:|---:|---|
| Java code volume | 708 | 40,317 | Main 637 / 30,357; Test 71 / 9,960 |
| Repository footprint (tracked files) | 758 | 52,210 | Maven dependencies/plugins: 12 / 2 |
| Layer distribution (main Java) | - | - | Domain 16 / 1,512; Business 480 / 19,406; Adapters 120 / 7,138; Infrastructure 20 / 2,282 |
| Architecture components | - | - | Domain entities 13; Domain exceptions 3; Use case controls 90; Input boundaries 85; Output boundaries 91; Business DTOs 195; Repository ports 11 |
| Adapter/infra components | - | - | Controllers 12; Presenters 40; ViewModels 40; Repository adapters 6; JPA entities 8; JPA repositories 6 |
| Frontend static assets | 38 | 9,906 | HTML 12 / 1,490; CSS 11 / 4,170; JS 15 / 4,246 |
| Database setup script | 1 | 455 | CREATE TABLE 8; index declarations 18; CREATE TRIGGER 4; INSERT INTO 13 |

- API surface summary:
  - Total controller endpoints: 51 (API + WebView routes)
  - API endpoints only: 39 (excluding `WebViewController`)
  - Web page routes: 12
  - HTTP method split (all): GET 29, POST 13, PUT 1, PATCH 3, DELETE 5
  - HTTP method split (API only): GET 17, POST 13, PUT 1, PATCH 3, DELETE 5

## 6) API groups

| Group | Base paths | Endpoints |
|---|---|---:|
| Auth | `/api/auth` | 2 |
| Catalog | `/api/products`, `/api/motorbikes`, `/api/accessories` | 11 |
| Cart | `/api/cart` | 3 |
| Orders | `/api/orders`, `/api/user/orders` | 4 |
| Admin | `/api/admin`, `/api/admin/users`, `/api/admin/orders` | 19 |
| Web pages | `/`, `/home`, `/login`, `/register`, `/cart`, `/checkout`, `/my-orders`, `/admin/*` | 12 |

## 7) Testing and quality

- Test classes: 71 (`src/test/java`).
- Annotated test cases (`@Test`): 393.
- Core test focus: business use cases (`67` files) and domain entities (`4` files).
- To run tests locally:

```powershell
./mvnw.cmd test
```

## 8) Demo credentials (seed)

- Seed accounts are inserted in `database-setup.sql` (`tai_khoan` table).
- Common demo usernames in current script: `admin2`, `user1`, `hayami`, `hayamij`.
- Example admin login often used in seeded data: `admin2` / `123`.
- Note: password formats in seed data are mixed (hashed/plain for legacy/demo scenarios).

## 9) License

MIT License (see `LICENSE`).
