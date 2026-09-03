# Developer Setup

Instructions for setting up a local development environment for `policy-machine-core`.

## Prerequisites

-   **JDK 21** (the project compiles and targets Java 21)
-   **Git** with submodule support

Maven itself does not need to be installed separately — the repo ships the Maven Wrapper (`./mvnw`).

## Clone the repository

This repo uses a git submodule (`grpc/protos`) for the gRPC proto definitions, so clone with submodules included:

```
git clone --recurse-submodules https://github.com/PM-Master/policy-machine-core.git
cd policy-machine-core
```

If you already cloned without `--recurse-submodules`:

```
git submodule update --init --recursive
```

## Build

This is a multi-module Maven reactor with three modules:

-   `core` - `pap`/`pdp`/`common`/`epp` interfaces plus the in-memory implementation
-   `neo4j` - the embedded Neo4j-backed `PolicyStore` implementation
-   `grpc` - the gRPC client/server implementation (depends on the `grpc/protos` submodule)

Build and install all modules to your local Maven repository:

```
./mvnw clean install
```

Skip tests for a faster build while iterating:

```
./mvnw clean install -DskipTests
```

## Run tests

```
./mvnw test
```

Tests run against all three modules. CI (`.github/workflows/build.yml`) runs `mvn -B verify` on every push/PR to `main`, so make sure `verify` passes locally before opening a PR.

## IDE setup

Any IDE with Maven multi-module support works.

If you update the `grpc/protos` submodule or the `grpc` module's `.proto` files, regenerate sources by rebuilding the `grpc` module (`./mvnw -pl grpc install`); generated sources can be found in `grpc/target/generated-sources`.

## Working with the protos submodule

`grpc/protos` is pinned to a specific commit of [policy-machine-protos](https://github.com/usnistgov/policy-machine-protos). To update it:

```
cd grpc/protos
git fetch origin
git checkout <commit-or-branch>
cd ../..
git add grpc/protos
git commit -m "Update protos submodule to <commit>"
```
