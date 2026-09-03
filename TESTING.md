# Testing

How to run tests for `policy-machine-core`.

## Test frameworks

All three modules (`core`, `neo4j`, `grpc`) use JUnit 5 (Jupiter). No external services are required to run the suite:

-   `neo4j`'s tests use the embedded Neo4j engine, started in-process per test.
-   `grpc`'s tests exercise the client/server code generated from the `grpc/protos` submodule directly, without a separate server process.

## Running the full suite

From the repo root:

```
./mvnw test
```

This runs unit tests across all three modules in the reactor.

To run the same checks CI runs (`.github/workflows/build.yml` on every push/PR to `main`):

```
./mvnw -B verify
```

## Running tests for a single module

Use Maven's `-pl` (project list) flag:

```
./mvnw -pl core test
./mvnw -pl neo4j test
./mvnw -pl grpc test
```

Add `-am` to also build any modules it depends on first (e.g. `neo4j` and `grpc` both depend on `core`):

```
./mvnw -pl neo4j -am test
```

## Running a single test class or method

```
./mvnw -pl core test -Dtest=MemoryPAPTest
./mvnw -pl core test -Dtest=MemoryPAPTest#testCreatePolicyClass
```

## Test locations

```
core/src/test/java
neo4j/src/test/java
grpc/src/test/java
```

Test resources (sample PML files, etc.) live under the corresponding `src/test/resources` directory.

## Before opening a PR

Run `./mvnw -B verify` from the repo root and make sure it passes — this mirrors what CI checks on every pull request against `main`.
