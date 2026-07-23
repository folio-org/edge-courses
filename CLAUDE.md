# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this module is

`edge-courses` is a FOLIO edge API module (Java 21, Spring Boot). It bridges third-party discovery
services and FOLIO's `mod-courses` module: it accepts unauthenticated/API-key requests, resolves them
to an Okapi tenant/token via `edge-common-spring`, calls `mod-courses` over HTTP, and returns the data
(often the raw JSON string from `mod-courses`, sometimes reshaped/paginated).

## Build, test, and lint

- Build (and generate sources from the OpenAPI spec): `./mvnw clean install` (or `mvn clean install`)
- Run tests only: `mvn test`
- Run a single test class: `mvn test -Dtest=CourseReserveServiceTest`
- Run a single test method: `mvn test -Dtest=CourseReserveServiceTest#methodName`
- Run the app locally: `mvn spring-boot:run` (listens on port 8080; uses `EphemeralStore` with
  `src/main/resources/ephemeral.properties` by default)

Test naming matters for Surefire: only `**/*IT.java`, `**/*Test.java`, `**/*Tests.java` are executed
(configured in `pom.xml`).

The OpenAPI generator plugin (`openapi-generator-maven-plugin`) runs during `generate-sources` and
produces the DTOs/API interfaces under `target/generated-sources` from
`src/main/resources/swagger.api/edge-courses.yaml`. **You must run a Maven build at least once (or
after editing the yaml/schemas) before the IDE/Claude will resolve `org.folio.courses.domain.dto.*`
and `org.folio.courses.rest.resource.*` classes** — they don't exist in `src/main`, they're generated.

## Architecture

Request flow: `CourseReservesController` (implements generated `CoursesApi` interface) →
`CourseReservesService` → `CourseClient` (a declarative HTTP interface, see below) → `mod-courses`.

- **API contract is defined in OpenAPI, not Java.** `src/main/resources/swagger.api/edge-courses.yaml`
  (with schemas split out under `swagger.api/schemas/*.json`) is the source of truth for endpoints,
  parameters, and response/error shapes. The generator config maps models to package
  `org.folio.courses.domain.dto` and API interfaces to `org.folio.courses.rest.resource`
  (`interfaceOnly=true`, so only interfaces are generated — controllers implement them by hand).
  When adding/changing an endpoint, edit the yaml/schemas first, then implement the resulting
  interface method in a controller.

- **`CourseClient`** (`client/CourseClient.java`) is a Spring 6 HTTP Interface Client
  (`@HttpExchange`/`@GetExchange`), not a Feign or RestTemplate client. It's turned into a bean via
  `HttpClientConfiguration`, which uses the `edgeHttpServiceProxyFactory` bean supplied by
  `edge-common-spring` (note `folio.exchange.enabled: false` in `application.yml` — edge-common-spring
  provides its own factory, so the module's own exchange auto-config is deliberately disabled).
  It returns raw `tools.jackson.databind.JsonNode` — most service methods just call `.toString()` and
  pass the JSON straight through rather than deserializing to typed DTOs, except where the module needs
  to inspect/reshape data (departments, instructors).

- **`CourseReservesService`** has two flavors of endpoint:
  - Pass-through (`getCoursesByQuery`, `getReservesByQuery`, `getReservesByInstanceId`): forwards the
    query params to `mod-courses` and returns the JSON response body as-is.
  - Derived/aggregated (`getDepartments`, `getInstructors`): fetches *all* courses from `mod-courses`
    (`limit=Integer.MAX_VALUE`), filters to courses considered "active" (via `isActiveCourse`, which
    checks the course listing's term start/end dates against `Instant.now()`), extracts and
    dedupes departments or instructors from the nested course graph, then paginates the result itself
    in-memory using the caller's `offset`/`limit`. There is no server-side pagination for these two
    endpoints — it's all done in Java after pulling the full course set.

- **`RequestQueryParametersMapper`** converts the generated `RequestQueryParameters` DTO into a
  `Map<String, Object>` for use as query params on `CourseClient` calls — only non-null/non-blank
  fields are included.

- **Errors**: `CoursesErrorHandler` (`@RestControllerAdvice`) catches `HttpStatusCodeException` from
  downstream `mod-courses` calls and `ConstraintViolationException` from validation, and converts them
  to the generated `Error` DTO with the appropriate HTTP status — downstream error responses are
  propagated with their original status code, not swallowed into a generic 500.

- **Tenant handling**: `TenantController` and tenant-related config come from `edge-common-spring`
  and `folio-spring-base`; `folio.tenant.validation.enabled: false` in `application.yml` disables
  strict tenant validation for this module.

## Testing conventions

- Integration tests extend `BaseIntegrationTests`, which boots the full Spring context
  (`@SpringBootTest` + `@AutoConfigureMockMvc`) and starts a WireMock server per test class, rebinding
  `EdgeClientProperties`' Okapi URL to WireMock via reflection in `@BeforeAll`. WireMock stub mappings
  live in `src/test/resources/mappings/*.json`, and stubbed response bodies in
  `src/test/resources/__files/**`.
- Use `doGet`/`doGetWithParam`/`doGetWithLimitAndOffset` helpers from `BaseIntegrationTests` for
  requests — they attach the required `X-Okapi-Tenant`, `X-Okapi-Url`, and `X-Okapi-Token` headers
  automatically (values from `TestConstants`).
- Test fixture JSON payloads (courses/reserves/departments sample responses) live under
  `src/test/resources/__files/**` and are reused across both WireMock stubs and direct unit tests
  (see `JsonSample`).
