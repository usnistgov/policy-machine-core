# Policy Machine Core — Production Readiness Assessment

**Project:** `gov.nist.csd.pm:policy-machine-core` (NIST reference implementation of the NGAC standard)
**Assessed revision:** `69cbd2d0` (tag `3.6.0`, master)
**Assessment date:** 2026-07-16
**Scope:** Readiness of this library for embedding in production systems by downstream projects.

---

## 1. Executive Summary

Policy Machine Core is a mature, well-factored reference implementation of NGAC with a clean
architectural separation (PAP / PDP / EPP), a purpose-built policy language (PML), and a passing
test suite of 824 tests. The core in-memory decision engine is algorithmically sound and uses
appropriate data structures (fastutil primitive collections) on its hot paths.

**Scoping note:** the in-memory implementation (`MemoryPAP`/`MemoryPolicyStore`) is *intended*
as a testing and single-threaded-embedding backend, not a production store. This assessment
evaluates it against that intent. The consequence is that **the production story rests on the
durable/remote backends (Neo4j, gRPC) — which are currently the weakest parts of the library.**

However, the library is **not yet production-grade as shipped**. The gaps are not in the NGAC
logic — they are in the engineering perimeter around it:

| Area | Verdict |
|---|---|
| Core NGAC model & decision engine (memory) | **Fit for intended purpose** (testing / single-threaded) — sound, tested; intent undocumented |
| Transaction handling | **Not ready** — unchecked exceptions bypass rollback; no isolation |
| Neo4j embedded backend (the durable production path) | **Not ready** — Java native deserialization (security risk), swallowed errors |
| gRPC client (`impl/grpc`, the remote production path) | **Experimental** — zero test coverage |
| Packaging & dependencies | **Not ready** — monolithic jar forces Neo4j server, gRPC, protobuf onto all consumers |
| Release & supply-chain hygiene | **Not ready** — JitPack-only, pom version drift, no signing/SBOM |
| Documentation | **Partial** — good README and PML docs; ~12% Javadoc coverage; backend intent/scope undocumented |

The single most consequential work for the project is **hardening a production backend path**
(Neo4j today, or a driver-based store) and **splitting the monolith into modules**
(core / neo4j / grpc), alongside documenting which backends are meant for what. Most other
items below are incremental hardening.

---

## 2. Architecture Overview (as found)

```
gov.nist.csd.pm.core
├── common      — nodes, prohibitions, events, exceptions (PMException hierarchy), tx interfaces
├── pap         — Policy Administration Point: modification, query, PML compiler/VM,
│                 obligations, operations, serialization (JSON + PML), admin policy
├── pdp         — Administrative PDP: adjudication + access checks wrapped around the PAP,
│                 PDPTx, event publishing
├── epp         — Event Processing Point: obligation matching & response execution
└── impl
    ├── memory  — MemoryPAP + MemoryPolicyStore (fastutil-backed, compensating-command rollback)
    ├── neo4j   — embedded Neo4j-backed PolicyStore
    └── grpc    — generated protobuf/gRPC client bindings for a remote PDP/EPP
```

- 429 main-source files, 151 test files; Java 21; ANTLR4-generated PML parser.
- `PAP` composes a `PolicyStore` with modifier/querier facades; `PDP` wraps `PAP` with
  per-operation privilege checks; `EPP` subscribes to PDP events and evaluates obligations.
- Access decisions: `AccessQuerier` → `UserEvaluator` / `TargetEvaluator` (visited-set-tracked
  DAG traversal, per-policy-class privilege resolution, prohibition resolution). This hot path
  is correct and efficient.

### Strengths worth calling out

- **Clean layering.** Store interface (`PolicyStore`) cleanly abstracts backends; PDP/PAP
  separation mirrors the NGAC standard's functional architecture.
- **Rich, specific exception hierarchy** (`NodeNameExistsException`, `AssignmentCausesLoopException`,
  `DisconnectedNodeException`, …) rather than string-only errors.
- **PML compiler** collects *all* compile errors with positions (`PMLCompilationException.getErrors()`)
  instead of failing on the first — good for tooling built on top.
- **824 passing tests, 0 failures/skips** across memory and Neo4j backends, PML, EPP, PDP
  adjudication, serialization.
- **Zero TODO/FIXME markers** in main source; consistent code style.
- **Graph integrity enforced at write time**: duplicate name/ID checks, assignment-loop checks,
  disconnected-node prevention, admin-policy protection.

---

## 3. Detailed Findings

### 3.1 Concurrency & Thread Safety — **undocumented scope, no guard rails**

There is **no synchronization primitive anywhere in `src/main`** (no `synchronized`, locks,
concurrent collections, atomics, or `volatile`). Given that the in-memory backend is *intended*
for testing and single-threaded embedding, this is acceptable **by design** for
`impl/memory` — plain `HashMap` / `Long2ObjectOpenHashMap` structures and the single mutable
`MemoryTx` counter + shared `TxCmdTracker` undo log are fine under a one-thread-at-a-time model.

The gaps are therefore about *contract*, not implementation:

- **The intent is documented nowhere.** Neither the README, Javadoc on `MemoryPAP`, nor the
  package docs say "testing/single-threaded only." The README's quickstart presents `MemoryPAP`
  as *the* way to start, so downstream services will (and do) reach for it as a production
  store. An undocumented scope restriction on an authorization library fails in the worst
  possible way: silent policy corruption or wrong access decisions under concurrent load.
- **No fail-fast guard.** Misuse is undetectable today. A cheap owner-thread check or
  "concurrent tx attempted" assertion in `MemoryTx`/`MemoryPolicyStore` would convert silent
  corruption into an immediate error, at zero cost to the intended use case.
- **The PDP/EPP layer is backend-agnostic and *is* the production path**, yet has its own
  thread-unsafe state: `PDP.eventSubscribers` is a plain `ArrayList` mutated by
  `addEventSubscriber` — unsafe if subscription happens while events are being published, even
  when backed by a thread-safe store. Whatever the backend, the PDP/PAP wrapper classes need a
  stated (and honored) concurrency contract.
- **No production-grade concurrent backend exists to point to instead** — see §3.11.

### 3.2 Transaction Semantics — **critical gap**

- **Rollback is skipped on unchecked exceptions.** `TxRunner.runTx`
  (`common/tx/TxRunner.java:13`), `PAP.runTx` (`pap/PAP.java:217`), and
  `PAP.deserialize` (`pap/PAP.java:183`) catch **only `PMException`**. Any
  `RuntimeException` (NPE, `IllegalArgumentException` — of which main source throws 59 —
  ANTLR errors, fastutil errors) propagates **without rollback and without resetting
  `MemoryTx.active`**, leaving the store with a half-applied transaction and a stale undo log
  that will be replayed into some *future* transaction's rollback.
- **No isolation.** Readers see uncommitted writes of an in-flight transaction (rollback is
  implemented as compensating commands over live state). Fine for single-threaded embedding;
  must be documented.
- **Nested rollback is all-or-nothing.** `MemoryTx.rollback()` zeroes the counter, so an inner
  `runTx` failure unwinds the entire outer transaction. Acceptable design, but undocumented.
- `MemoryPolicyStore.rollback()` keeps applying compensating commands even conceptually after
  one fails only by wrapping into `PMException` — a failed compensating command aborts the loop
  (`MemoryPolicyStore.java:73-79`), potentially leaving partial rollback with no recovery signal
  beyond the wrapped exception.

**Fix shape:** `catch (Exception)` (or `finally`-based state machine) in every tx runner,
re-throw after rollback; document isolation level; consider copy-on-write snapshots instead of
compensating commands for the memory store.

### 3.3 Security

- **Java native deserialization in the Neo4j backend.**
  `impl/neo4j/embedded/pap/store/Neo4jUtil.deserialize` (`Neo4jUtil.java:105-123`) rehydrates
  arbitrary objects from hex-encoded `ObjectInputStream` payloads stored in the database, with a
  caller-supplied `ClassLoader` and **no allow-list filter** (`ObjectInputFilter`). Anyone able
  to write to the Neo4j store (or supply a crafted database file) gets a classic gadget-chain
  RCE primitive. Same method calls `e.printStackTrace()` — the only console output in the library.
- **ID generation.** `RandomIdGenerator` (`pap/id/RandomIdGenerator.java:16`) returns
  `Math.abs(secureRandom.nextLong())`: `Math.abs(Long.MIN_VALUE)` is negative (1-in-2⁶⁴, but
  trivially avoidable), and random IDs are only collision-checked at creation
  (`NodeIdExistsException`) — a collision surfaces as a hard, unretried error to the caller.
  Name-based checks make this a nuisance rather than a correctness bug, but a retry loop or
  sequential generator would remove the failure mode.
- **PML execution is fully privileged at the PAP level.** `PAP.executePML` runs with no access
  checks (by design; the PDP variant checks). The Javadoc does not warn about this asymmetry —
  a consumer wiring `pap.executePML` into a request path has created a bypass. Needs prominent
  documentation.
- **No resource limits on PML.** A `while` loop in PML executed via `executePML` can spin
  forever; no statement budget, timeout, or recursion cap was found. For any deployment where
  policy authorship is less trusted than the host process, this is a DoS vector.

### 3.4 Robustness & Error Handling

- 59 `throw new RuntimeException/IllegalArgument/IllegalState/UnsupportedOperation` sites in
  main source, including wrapping checked `PMException` into `RuntimeException` inside stream
  lambdas (`pdp/query/ObligationsQueryAdjudicator.java:77`,
  `pdp/query/GraphQueryAdjudicator.java:211`) — these bypass both the declared `throws PMException`
  contract *and* the rollback handling from §3.2.
- Recursive graph algorithms without depth limits: `DepthFirstGraphWalker.walkInternal`,
  `BreadthFirstGraphWalker.walkInternal` (recursive despite the name), and
  `AccessQuerier.getAscendants` / `computeSubgraphPrivileges` recurse per edge/level —
  `StackOverflowError` on pathologically deep hierarchies (tens of thousands of levels).
- **Generic graph walkers have no visited set.** Both `DepthFirstGraphWalker` and
  `BreadthFirstGraphWalker` re-visit nodes reachable via multiple paths. In diamond-heavy DAGs
  (common in NGAC: many ascendants sharing containers) traversal cost grows with the number of
  *paths*, which is exponential in the worst case. The hot decision path (`TargetEvaluator`,
  `CachedTargetEvaluator`) does track visited nodes, so `computePrivileges` is safe — but
  `computePersonalObjectSystem`, `computeRequiredAttributeSets`, and modifier-side walks use the
  generic walkers.
- `EPP.processEvent` catches and logs all exceptions per obligation (good — one bad obligation
  can't break event delivery) but obligation-fetch failure silently degrades to "no obligations
  evaluated" (`EPP.java:61-65`) — an availability-vs-fail-closed decision that should be a
  documented, configurable policy for a security component.

### 3.5 Performance & Scalability

- Memory store uses fastutil primitive maps on node/graph structures — good.
- `CachedTargetEvaluator` memoizes across multi-target queries — good.
- No decision cache exists across calls; every `computePrivileges` re-walks the user side.
  Acceptable for a reference implementation; consumers should know (document it).
- `computeACL` is O(users × graph) by design; fine, but worth a Javadoc warning.
- No benchmarks exist in the repo (no JMH module). There is no published guidance on graph sizes
  the memory implementation handles comfortably.

### 3.6 Packaging & Dependency Management — **high-impact gap**

The single artifact forces every consumer to inherit:

- `org.neo4j:neo4j` **2026.01.3 — the full embedded Neo4j server** (a very large transitive
  tree: Netty, Lucene, Scala-free but dozens of jars) even if they only use `MemoryPAP`.
- `io.grpc` + `com.google.protobuf` even if they never talk to a remote PDP.
- `gson` 2.9.0 (2022-era; current is 2.11+) and `commons-codec` 1.15.

Consequences: dependency-convergence conflicts in host applications (Neo4j and gRPC are
notorious for Netty/guava clashes), bloated deploy artifacts, and a needlessly large CVE
surface to monitor.

Additional issues:

- **`pom.xml` version is `3.3.0` while HEAD is tagged `3.6.0`** — the coordinates inside the
  artifact do not match the release tag. Under JitPack (which builds from tags) this yields
  artifacts whose embedded pom disagrees with their declared version.
- Distribution is **JitPack-only**; not on Maven Central. No GPG signing, no sources/javadoc
  jars configured, no SBOM (CycloneDX/SPDX), no reproducible-build configuration.
- No `CHANGELOG.md`; version history is only recoverable from git.
- `logback-classic` is correctly test-scoped and only `slf4j-api` leaks to consumers — good.

### 3.7 API Stability

- Git history shows repeated breaking refactors between 3.x tags (`UserContext`/`TargetContext`
  reworked in #230/#233/#238, operation execute signature changed in #237, PDP return type
  changed in #239) with **no deprecation cycle** and no documented semver policy.
- Public surface is very broad: nearly every internal class is `public` (store internals,
  visitors, tx command classes). No `@API`-style annotations, no `internal` package convention.
  Consumers cannot tell the supported API from implementation detail, which makes every internal
  change a potential break.

### 3.8 Testing & Quality Gates

- **Good:** 824 tests, 0 failures, covering both store backends, the PML language, EPP
  obligations, PDP adjudication, serialization round-trips.
- **Gaps:**
  - No code-coverage measurement (no JaCoCo); coverage is unknown.
  - Zero tests for the entire `impl/grpc` package.
  - No concurrency tests (consistent with §3.1 — but once a contract is defined it needs tests).
  - No property-based/fuzz testing of the PML parser (ANTLR parsers are classic fuzz targets;
    a hostile policy string should never produce anything but `PMLCompilationException`).
  - No performance regression tests or benchmarks.
  - No mutation testing.
- CI (`.github/workflows/maven.yml`) builds and tests on push/PR only. Issues:
  - `permissions: write-all` — grossly over-privileged for a build job.
  - `actions/checkout@v3` / `setup-java@v3` are outdated majors.
  - No static analysis (SpotBugs/ErrorProne/Checkstyle), no CodeQL, no dependency scanning
    (Dependabot/Renovate absent), no release workflow.

### 3.9 Observability

- SLF4J is used in exactly **2 files** (EPP and one other). The PAP/PDP core paths — the ones a
  production operator most needs to see (admin mutations, decision denials, obligation firings) —
  emit nothing.
- No audit-log hook. For an access-control component, a structured, pluggable audit trail of
  decisions and administrative changes is a de-facto production requirement (and an NGAC
  architectural expectation — the RAP/EPP side of the standard).
- No metrics hooks (decision latency, graph size, cache behavior).

### 3.10 Documentation

- README: good quickstart with Java and PML examples. `docs/pml.md`: solid language reference.
- Javadoc exists in only **53 of 429** main-source files (~12%). Key entry points (`PDP.runTx`,
  `PAP.bootstrap`, serialization) are documented; almost everything else is not.
- Missing: architecture guide, concurrency/transaction contract, backend selection guide,
  threat model / secure-deployment notes, migration notes between 3.x versions, Javadoc
  publishing (no javadoc jar, no hosted site).

### 3.11 Production Backend Story — **the central gap**

Accepting that the in-memory store is a testing/single-threaded backend reframes the readiness
question: **what does a production consumer actually deploy on?** Today the options are:

- **`impl/neo4j` (embedded)** — the only durable backend. It carries the native-deserialization
  issue (§3.3), swallows errors, is embedded-only (single JVM owns the database files; no
  server/driver mode), and its concurrency behavior atop Neo4j transactions is undocumented and
  untested under load.
- **`impl/grpc`** — delegates to a remote PDP service (presumably `policy-machine-server`),
  which moves the storage/concurrency problem out of this library. Architecturally this is the
  most credible production path, but the client here has **zero tests** and no documentation.
- **Implement your own `PolicyStore`** — the interface is clean and this is a legitimate
  design intent for a reference implementation, but there is no implementer's guide, no
  contract tests (a reusable `PolicyStore` TCK), and no documentation of tx/isolation
  obligations a store must satisfy.

None of these is production-ready as shipped, and the library does not tell consumers which
path is intended for what. For a reference implementation, "bring your own store, here is the
TCK to validate it" may be the right answer — but it must be an *explicit, supported* answer.

---

## 4. Per-Component Readiness Verdicts

| Component | Verdict | Blocking issues |
|---|---|---|
| `impl/memory` *(judged as a testing / single-threaded backend)* | **Fit for purpose, with caveats** | intent undocumented; no fail-fast on concurrent misuse; tx rollback on unchecked exceptions (§3.2) |
| `pap` core (backend-agnostic) | **Beta** | tx runners skip rollback on unchecked exceptions; no stated concurrency contract |
| `pdp` / `epp` | **Beta** | same tx issues; subscriber list thread safety; fail-open obligation fetch |
| PML compiler & runtime | **Beta** | resource limits (§3.3); fuzz coverage |
| `impl/neo4j` *(the durable production path)* | **Not production-ready** | native deserialization RCE surface; error swallowing; embedded-only; untested under concurrency |
| `impl/grpc` *(the remote production path)* | **Experimental** | zero tests; no docs |
| Custom `PolicyStore` route | **Unsupported in practice** | no implementer's guide or contract-test kit (§3.11) |
| Packaging/release | **Not production-ready** | monolith deps; version drift; JitPack-only |

---

## 5. TODO List for Production Grade

### P0 — Correctness & safety blockers

1. ✅ **DONE — Fix rollback on unchecked exceptions.** In `TxRunner.runTx`, `PAP.runTx`, and
   `PAP.deserialize`, catch `Exception` (rollback, then rethrow — wrapping non-PM exceptions in
   `PMException` or rethrowing as-is). Add tests: a `RuntimeException` mid-tx must leave the
   store byte-identical to pre-tx state and reset `MemoryTx` counters.
2. ✅ **DONE — Document backend intent and add misuse guard rails.** State explicitly (README, Javadoc on
   `MemoryPAP`/`MemoryPolicyStore`, package-info) that the in-memory backend is for testing and
   single-threaded embedding only, and say what the production alternatives are. Add a cheap
   fail-fast check (owner-thread or concurrent-tx assertion) in `MemoryTx`/`MemoryPolicyStore`
   so concurrent misuse throws instead of silently corrupting policy. Fix the backend-agnostic
   PDP layer independently (`CopyOnWriteArrayList` for `PDP.eventSubscribers`) and document the
   concurrency contract of `PAP`/`PDP` for any backend.
3. **Remove or harden Java native deserialization in `Neo4jUtil`.** Replace with the existing
   JSON/protobuf representations, or at minimum install a strict `ObjectInputFilter` allow-list.
   Remove `e.printStackTrace()`.
4. ✅ **DONE — Eliminate `RuntimeException` wrapping of `PMException`.** Both wrap sites
   (`ObligationsQueryAdjudicator.filterObligations` and `GraphQueryAdjudicator.filterNodes`, the
   latter's dead `else { throw new RuntimeException(e); }` fallback) reimplemented the same
   "smuggle a checked exception out of a `removeIf` lambda" idiom by hand. Extracted it once into
   `Adjudicator.filterUnauthorized`, a shared, type-safe helper on the common adjudicator base
   class: it wraps a caught `PMException` in the existing unchecked carrier `PMRuntimeException`
   inside the lambda, catches it immediately outside `removeIf`, and unconditionally unwraps and
   rethrows the real `PMException` — no `instanceof`/fallback branch needed, since the helper is
   the only place that constructs `PMRuntimeException`. Both adjudicators now declare
   `throws PMException` on the filtering method instead of leaking an undeclared
   `RuntimeException`. Added regression tests (`ObligationsQueryAdjudicatorTest`,
   `GraphQueryAdjudicatorTest`) asserting a non-`UnauthorizedException` `PMException`
   (`NodeDoesNotExistException`, triggered by a stale/non-existent node reference) propagates as
   a checked `PMException`, not `RuntimeException`.
   **Audit of the other 57 unchecked-throw sites:** of 59 `throw new
   RuntimeException/IllegalArgumentException/IllegalStateException/UnsupportedOperationException`
   sites in `src/main`, ~40 are reachable mid-transaction (operation-arg validation/casting in
   `pap/operation`, PML execution, and query-adjudicator calls made through
   `PDP.runTx`/`PDPTx.TxExecutor`); the remainder are gRPC client stub/marshalling code and one
   PML pretty-printer used only for gRPC wire serialization, none of which run inside a local
   transaction boundary in this codebase. Since P0.1's `catch (Exception e)` sits at the
   outermost boundary of every transaction (`TxRunner.runTx`, `PAP.runTx`, `PAP.deserialize`),
   every mid-tx-reachable site is already covered by rollback + `PMException`-wrapping
   regardless of origin — no further code change was needed for the audit portion.
5. **Fix `RandomIdGenerator`**: guard `Long.MIN_VALUE`, and either retry on collision at the
   `GraphModifier` layer or switch the default to a monotonic/sequence-based generator.

### P1 — Required for a supportable production release

6. **Modularize the build**: `pm-core` (no Neo4j, no gRPC), `pm-neo4j`, `pm-grpc`. Consumers of
   `MemoryPAP` must not inherit the Neo4j server. Alternatively (short-term) mark Neo4j and gRPC
   deps `<optional>true</optional>`.
7. **Release hygiene**: align pom version with tags (fix the 3.3.0-vs-3.6.0 drift); publish to
   Maven Central with sources + javadoc jars and GPG signatures; add `CHANGELOG.md` (Keep a
   Changelog format); generate an SBOM in CI.
8. **Dependency updates & scanning**: bump gson (≥2.11), commons-codec; add Dependabot/Renovate;
   add OWASP dependency-check or `mvn versions` gates in CI.
9. **CI hardening**: drop `permissions: write-all` to `contents: read`; update actions to
   current majors; add JaCoCo with a published coverage report; add CodeQL + SpotBugs/ErrorProne;
   add a tag-triggered release workflow.
10. **Test the gRPC implementation** (or explicitly label it experimental and exclude it from
    the release artifact until tested). Since the remote-PDP model is the most credible
    concurrent production deployment, this path deserves priority over hardening the memory
    store.
11. **Publish a `PolicyStore` contract-test kit (TCK) and implementer's guide.** If "bring
    your own durable store" is the supported production answer for a reference implementation,
    ship the reusable test suite that validates a custom store's graph, prohibition, obligation,
    and transaction semantics, and document the tx/isolation obligations a store must meet.
12. **Durable store story**: the Neo4j backend is embedded-only; evaluate a bolt/driver-based
    backend or document the embedded operational model (backups, upgrades, file locking) and
    its concurrency behavior. With the memory store scoped to testing, this is the library's
    only first-party durable path and needs to be treated as such.
13. **Iterative graph walkers with visited sets.** Convert `DepthFirstGraphWalker` /
    `BreadthFirstGraphWalker` to explicit-stack/queue iteration and add visited tracking
    (opt-out flag if any caller genuinely needs per-path propagation); same for
    `AccessQuerier.getAscendants` / `computeSubgraphPrivileges`.
14. **Audit & decision logging hooks.** Add a pluggable audit interface (decision made, admin
    mutation applied, obligation fired) with a no-op default; wire SLF4J debug logging through
    PDP adjudication and PAP modification paths.
15. **PML resource limits**: configurable statement-count budget and loop/recursion caps for
    `executePML`; document the PAP-vs-PDP privilege asymmetry prominently.
16. **Make EPP failure policy explicit**: configurable fail-open vs fail-closed when obligations
    cannot be fetched or a response fails.

### P2 — Maturity & ecosystem

17. **API surface control**: introduce an `internal` package convention (or JPMS modules) and
    document what is supported API; adopt a semver + deprecation policy for 3.x → 4.x.
18. **Javadoc the public API** (target: 100% of intended-public types) and publish the Javadoc
    site; write an architecture/concepts guide and a secure-deployment guide (threat model,
    trusted vs untrusted policy authors, which backend for which deployment shape).
19. **Benchmarks**: JMH module measuring `computePrivileges`, `computeACL`,
    `computePersonalObjectSystem` across graph sizes; publish guidance on memory-backend limits.
20. **PML parser fuzzing** (e.g., Jazzer in CI) with the invariant "any input → result or
    `PMLCompilationException`, never other throwables or hangs."
21. **Decision caching layer** (optional module) with invalidation on graph mutation, for
    read-heavy deployments.
22. **Coverage-guided test expansion** once JaCoCo lands — prioritize `pap/pml` runtime,
    serialization edge cases, and rollback paths of every `TxCmd`.

---

## 6. Bottom Line

Judged against its intended scope — **the in-memory backend for testing and single-threaded
embedding, durable/remote backends for production** — the library's core is in good shape: the
NGAC engine is sound and well-tested, and the memory implementation is fit for its purpose once
that purpose is written down and guarded (P0-2). The P0 list is small and mechanical.

The real production gap is that **neither of the intended production paths is ready**: the
Neo4j backend has a security-relevant deserialization surface and no operational story, and the
gRPC client is untested. Until one of those paths (or a supported bring-your-own-`PolicyStore`
route with a contract-test kit) is hardened, downstream projects will keep defaulting to
`MemoryPAP` in production — precisely the misuse the library's design intends to avoid. None of
the findings suggest architectural rework; this is hardening and documentation work, most of it
well-bounded.
