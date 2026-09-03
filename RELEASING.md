# Releasing

This project publishes to Maven Central under the `gov.nist.ngac.pm` groupId. Releases are cut manually from `main`.

## 1. Bump the version

Bump the version across the root pom and all modules with the `versions-maven-plugin` (no setup required, resolved from Maven Central on first use):

```
mvn versions:set -DnewVersion=<new-version> -DprocessAllModules=true -DgenerateBackupPoms=false
git diff
git add pom.xml core/pom.xml neo4j/pom.xml grpc/pom.xml
git commit -m "Bump version to <new-version>"
git push
```

## 2. Tag the release

```
git tag <new-version>
git push origin <new-version>
```

## 3. Publish to Maven Central

Publishing uses the `release` Maven profile, which signs artifacts with GPG and deploys them via the `central-publishing-maven-plugin` (`autoPublish` is enabled, so a successful deploy is published immediately, no manual step in the Central portal required).

Requires:

-   A GPG key available to `maven-gpg-plugin` for signing.
-   Credentials for the `central` server configured in `~/.m2/settings.xml` (matches `publishingServerId` in the root `pom.xml`).

```
mvn clean deploy -P release
```
