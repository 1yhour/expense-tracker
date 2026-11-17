# AGENTS.md

## Project
Expense tracker REST API. Spring Boot 4.1.2, Java 25, Maven (wrapper), PostgreSQL, Spring Security + JWT.
Status: entities exist for `users`, `user_settings`, `categories`, `transactions`, `budgets` (plus a `test` scaffold table); no repositories, services, or controllers yet.

## Commands (Windows PowerShell)
- Run app: `.\mvnw.cmd spring-boot:run`
- Compile: `.\mvnw.cmd -q compile`
- All tests: `.\mvnw.cmd test`
- Single test: `.\mvnw.cmd -Dtest=ExpenseTrackerApplicationTests test`
- Full gate (no CI/lint exists): `.\mvnw.cmd verify`
- Start DB first: `docker compose up -d` (required for any `@SpringBootTest`)

## Package and layout
- Root package is `com.testing.expense_tracker` (derived from `artifactId`). Do not use `com.expense.tracker`.
- Layer-first MVC under `com.testing.expense_tracker`: `config/`, `controller/`, `service/` (+ `service/impl/`), `repository/` (+ `repository/projection/`), `entity/`, `dto/<domain>/`, `mapper/`, `security/`, `exception/`, `response/`, `enums/`, `util/`. Do not reintroduce feature packages (`user/entity/`, `budget/service/`, ...).
- `entity/AuditableEntity` (MappedSuperclass) supplies `created_at`/`updated_at`; extend it for new tables. `enums/TransactionType` is the shared INCOME/EXPENSE enum.

## Gotchas
- Spring Boot 4 starter names: use `spring-boot-starter-webmvc` (not `-web`); test starters are `-webmvc-test`, `-data-jpa-test`, `-validation-test`.
- DB is `expense_tracker` on host port `5433` (container `expense-tracker`). If a pre-existing volume was initialized with the old `expense-tracker` name, `POSTGRES_DB` is ignored and the app fails; create it with `docker exec expense-tracker psql -U postgres -c "CREATE DATABASE expense_tracker;"`.
- "Unable to determine Dialect without JDBC metadata" at startup means the DB is unreachable or the database name does not exist — it is not a Hibernate dialect/config problem.
- Credentials `postgres` / `password` are hardcoded in `application.properties`; keep any JWT secret in environment variables, never in source.
- `spring.jpa.hibernate.ddl-auto=update`; no Flyway/Liquibase. Schema changes are applied automatically — do not add migrations expecting them to run.
- `ExpenseTrackerApplicationTests.contextLoads` boots the full context and hits real PostgreSQL, so tests fail without the compose container.
- Moving/renaming a package without `clean` leaves old `.class` files in `target/classes`; Hibernate then fails with "Entity classes [...] share the entity name". Run `.\mvnw.cmd clean test`, not just `test`.
- Lombok `annotationProcessorPaths` are declared per-execution in `maven-compiler-plugin`; any new annotation processor must be added to both `default-compile` and `default-testCompile`.
- JWT is planned via `spring-boot-starter-oauth2-resource-server` (already in `pom.xml`). Avoid adding a second JWT stack (jjwt) or `MapStruct`; use manual mapping.
- `management.endpoints.web.exposure.include` in `application.properties` is inert — no actuator dependency.
- `DatabaseConnectionTests` writes to `users` inside a rolled-back transaction; it still requires the DB container.
- The dev DB has a leftover `test` table from the deleted scaffold entity. Nothing maps to it, so it is safe to drop.
- Soft delete exists only as a nullable `deleted_at` on `categories` and `transactions`; no `@SQLRestriction`, so queries must filter `deleted_at is null` explicitly.
- Budgets are unique per `(user_id, period_month, category_id)`, but PostgreSQL treats NULLs as distinct — the unique constraint does not prevent duplicate overall budgets (`category_id` null). Enforce that in the service layer.
- `period_month` must be the first day of the month and amounts must be positive; both are DB check constraints, so invalid values fail at flush time, not at validation time.

## Conventions
- Layers: Controller → Service (interface + impl) → Repository → Entity. DTOs at API boundaries only; never expose entities.
- Constructor injection; `@Transactional` on mutating service methods.
- `BigDecimal` for all monetary values.
- Scope every expense/budget query to the authenticated principal; never trust client-supplied user IDs for ownership.
- Enums for roles and fixed types; pagination + sorting on collection endpoints.
- Centralize errors in `common/exception` with `@RestControllerAdvice` and a consistent response envelope in `common/response`.
- BCrypt for passwords; stateless JWT authentication; protect endpoints by default.
