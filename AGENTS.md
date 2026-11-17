# AGENTS.md

## Project
Expense tracker REST API. Spring Boot 4.1.2, Java 25, Maven (wrapper), PostgreSQL, Spring Security + JWT.
Status: fresh scaffold — only `ExpenseTrackerApplication`, a placeholder entity, and `contextLoads` test exist.

## Commands (Windows PowerShell)
- Run app: `.\mvnw.cmd spring-boot:run`
- Compile: `.\mvnw.cmd -q compile`
- All tests: `.\mvnw.cmd test`
- Single test: `.\mvnw.cmd -Dtest=ExpenseTrackerApplicationTests test`
- Full gate (no CI/lint exists): `.\mvnw.cmd verify`
- Start DB first: `docker compose up -d` (required for any `@SpringBootTest`)

## Package and layout
- Root package is `com.testing.expense_tracker` (derived from `artifactId`). Do not use `com.expense.tracker`.
- Package by feature under `com.testing.expense_tracker`: `config/`, `security/`, `auth/`, `user/`, `expense/`, `category/`, `budget/`, `common/{exception,response,util}/`.

## Gotchas
- Spring Boot 4 starter names: use `spring-boot-starter-webmvc` (not `-web`); test starters are `-webmvc-test`, `-data-jpa-test`, `-validation-test`.
- DB is `expense_tracker` on host port `5433` (container `expense-tracker`). If a pre-existing volume was initialized with the old `expense-tracker` name, `POSTGRES_DB` is ignored and the app fails; create it with `docker exec expense-tracker psql -U postgres -c "CREATE DATABASE expense_tracker;"`.
- "Unable to determine Dialect without JDBC metadata" at startup means the DB is unreachable or the database name does not exist — it is not a Hibernate dialect/config problem.
- Credentials `postgres` / `password` are hardcoded in `application.properties`; keep any JWT secret in environment variables, never in source.
- `spring.jpa.hibernate.ddl-auto=update`; no Flyway/Liquibase. Schema changes are applied automatically — do not add migrations expecting them to run.
- `ExpenseTrackerApplicationTests.contextLoads` boots the full context and hits real PostgreSQL, so tests fail without the compose container.
- Lombok `annotationProcessorPaths` are declared per-execution in `maven-compiler-plugin`; any new annotation processor must be added to both `default-compile` and `default-testCompile`.
- JWT is planned via `spring-boot-starter-oauth2-resource-server` (already in `pom.xml`). Avoid adding a second JWT stack (jjwt) or `MapStruct`; use manual mapping.
- `management.endpoints.web.exposure.include` in `application.properties` is inert — no actuator dependency.
- `entity/Test.java` (table `"Test"`) is a throwaway scaffold, not a domain model.

## Conventions
- Layers: Controller → Service (interface + impl) → Repository → Entity. DTOs at API boundaries only; never expose entities.
- Constructor injection; `@Transactional` on mutating service methods.
- `BigDecimal` for all monetary values.
- Scope every expense/budget query to the authenticated principal; never trust client-supplied user IDs for ownership.
- Enums for roles and fixed types; pagination + sorting on collection endpoints.
- Centralize errors in `common/exception` with `@RestControllerAdvice` and a consistent response envelope in `common/response`.
- BCrypt for passwords; stateless JWT authentication; protect endpoints by default.
