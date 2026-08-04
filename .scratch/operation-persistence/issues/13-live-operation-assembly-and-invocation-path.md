Status: resolved

## Summary

Implement the read-path assembly logic in `OperationsQuerier` per [design doc §6](../../../docs/operation-persistence-design.md#6-live-operation-assembly-and-read-path).

## Scope

- Add `OperationKind getOperationKind(String name) throws PMException` to `OperationsQuery`/`OperationsStore`, throwing `OperationDoesNotExistException` on a miss. `MemoryOperationsStore` derives it from the held object (`!(op instanceof PMLOperation) ? NATIVE : PML`); `Neo4jEmbeddedOperationsStore` reads the `operation_kind` property from ticket 12.
- Implement `OperationsQuerier.getOperation(name)` exactly per the design doc's shape:
  ```java
  public Operation<?> getOperation(String name) throws PMException {
      if (nativeOperationRegistry.isProtected(name)) {
          return nativeOperationRegistry.get(name);
      }
      if (store.operations().getOperationKind(name) == OperationKind.NATIVE) {
          return nativeOperationRegistry.get(name);
      }
      return store.operations().getOperation(name);
  }
  ```
  Applies uniformly to **both** backends — no `MemoryOperationsStore` shortcut for `NATIVE` rows, even though it holds the live object.
- Update `getOperations()`/`getOperationNames()` (bulk) with the same per-row branch: list from the store, resolve each per-entry, then append protected built-ins from the registry.
- Confirm and leave unchanged: `ObligationsQuerier.getObligation(name)` has no kind branch, always goes through `store.obligations().getObligation(name)` (ticket 09's mechanism). `OperationExecutor`/`PDPTx`/`TxExecutor` need zero changes — verify this by running their existing test suites unmodified against the new `getOperation` path.
- Tests: a native op resolves identically whether read via `MemoryPAP` or `Neo4jEmbeddedPAP`; a protected built-in resolves without any store round-trip; bulk listing returns the right mix of kinds.

## Depends on

Ticket 08 (registry), ticket 09 (PML read path), ticket 10 (interface shape, registry reference on `OperationsQuerier`), ticket 12 (Neo4j `operation_kind` property to read).

## Out of scope for this ticket

Any change to `OperationExecutor`, `PDPTx`, or `TxExecutor` — the design doc is explicit these need zero changes; if this ticket's implementation finds otherwise, stop and flag it rather than silently expanding scope.

## Comments

Landed together with ticket 12 (see that ticket's comment for why). `OperationExecutor`/`PDPTx`/`TxExecutor` needed zero changes as predicted — confirmed by the full suite passing unmodified.

- `OperationKind getOperationKind(String name)` added to `OperationsQuery`/`OperationsStore`, throwing `OperationDoesNotExistException` on a miss. `MemoryOperationsStore` derives it from the held object; `Neo4jEmbeddedOperationsStore` reads the `operation_kind` property (ticket 12). Two other `OperationsQuery` implementers needed new methods to keep compiling: `OperationsQueryAdjudicator` delegates to `pap.query()`; `GrpcOperationsQuerier` throws `UnsupportedOperationException` (the `Signature` proto has no kind discriminator — gRPC persistence is out of scope for this whole map).
- `OperationsQuerier.getOperation` implemented exactly per the design doc's shape. Bulk `getOperations()`/`getOperationNames()` rewritten to no longer be store-only: they now prepend `nativeOperationRegistry.getProtectedOperations()`/`getProtectedNames()` (two new internal-only accessors) and resolve every persisted row per-name via the same NATIVE-registry/PML-store branch — this is a genuine behavior change from what ticket 10 shipped (which was deliberately store-only), called for explicitly by the design doc's §6.
- **Fallout from bulk now including protected built-ins, found by the test suite**: `PAP.isPolicyEmpty()`'s `opsEmpty` check used to rely on `query().operations().getOperations().isEmpty()`, which is never empty once built-ins are always included — every `bootstrap()` call would have started throwing `BootstrapExistingPolicyException`. Fixed by checking `policyStore().operations().getOperationNames().isEmpty()` directly (persisted rows only), mirroring the existing "ignore admin nodes" pattern in the same method. `RoutinesQuerierTest.testGetAdminRoutineNames` had an exact-set assertion on `getOperationNames()` that also needed updating to include `AdminOperations.ADMIN_OPERATIONS`' names.
- **Pre-existing round-trip bug surfaced by wiring Neo4j reads through real recompilation for the first time**: `MatchesOperationPattern`'s single-arg constructor (bare `performs matches "x"`, no `on (...)` clause) synthesizes a trivial `OnPattern` internally so match-time code always has a func to invoke, but `toFormattedString()` unconditionally re-emitted that synthetic `on (...) { return true }` clause — so recompiling the persisted text always took the `on`-clause branch in `CreateObligationStmtVisitor`, which requires the referenced operation name to exist (needed there only to type-check the on-clause's event params). This broke any obligation using a `MatchesOperationPattern` opName that isn't a real operation (several existing tests use fixture names like `""`/`"test_event"` purely for storage-round-trip testing, never for functional EPP matching). Fixed by tracking whether an `on` clause was actually authored (new `explicitOnPattern` field) and only emitting it in that case — a bare `matches "x"` now round-trips back to a bare `matches "x"`, deferring the operation-existence question to match time exactly like the original hand-authored PML would. `Neo4j`-only bug: `MemoryPAP` never recompiles obligations (ticket 09), so this was invisible on that backend.
- Full suite (`mvn -o test`): 948/948 passing on both backends.
- **Added after `/code-review` flagged the ticket's own test bullet as unmet**: `OperationsQuerierTest.testBulkListingMixesNativeAndPmlAndProtectedOperations` (parameterized, runs on both backends) covers "bulk listing returns the right mix of kinds"; `OperationsQuerierUnitTest.testProtectedBuiltinResolvesWithoutStoreRoundTrip` (new file) covers "a protected built-in resolves without any store round-trip" via a `PolicyStore` whose every method throws; `NativeOperationRegistryTest` gained three tests for the new `getProtectedOperations()`/`getProtectedNames()` accessors. "A native op resolves identically whether read via MemoryPAP or Neo4jEmbeddedPAP" was already covered structurally by the existing parameterized `GetAdminOperation.testSuccess`. Full suite: 955/955.
