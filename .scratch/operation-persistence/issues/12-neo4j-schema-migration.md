Status: resolved

## Summary

Implement the combined Neo4j schema per [design doc §5](../../../docs/operation-persistence-design.md#5-neo4j-schema).

## Scope

- Rename `OPERATION_LABEL` from `Label.label("AdminOp")` to `Label.label("Operation")`.
- Add `operation_kind` property (new `OperationKind` enum: `NATIVE`/`PML`, stored as `.name()`) to every `Operation` row.
- Add `pml_text` property on `PML`-kind `Operation` rows and all `Obligation` rows, replacing `DATA_PROPERTY` (`"data"`) usage in these two stores only. Leave `DATA_PROPERTY` untouched elsewhere (e.g. `setResourceAccessRights`).
- `NATIVE`-kind `Operation` rows: `name` + `operation_kind` only, no body property.
- Add three obligation-author properties: `author_id` (long), `author_name` (String), `author_process` (String) — each written only when the corresponding `NodeUserContext` field is non-null/non-sentinel. Read path reconstructs via the matching `NodeUserContext.of(...)` overload based on which are present.
- Rewire `Neo4jEmbeddedOperationsStore`/`Neo4jEmbeddedObligationStore`'s write path to use ticket 09's `toString()`/narrow-compile mechanism instead of `Neo4jUtil.serialize`/`deserialize`.
- Delete `Neo4jUtil.serialize`/`deserialize` and `Neo4jUtilTest` outright (grep first to reconfirm no other callers exist at implementation time — the design doc's grep was done at planning time, not implementation time).
- Confirm no new index is needed: `Neo4jEmbeddedPolicyStore.createIndexes`'s existing `operation_name_index` (declared against label `Operation`) starts applying for real once the label rename lands — verify with an `EXPLAIN`/profile check that lookups by name are now indexed, not `tx.findNode` unindexed scans.
- Migration note: this is a breaking schema change for existing Neo4j databases (old `"AdminOp"`-labeled, byte[]-serialized rows). Per the ADR, no migration tooling is being built — confirm this is still acceptable before merging, and flag it in the PR description.

## Depends on

Ticket 08 (registry — native rows carry no body, so nothing to serialize for them), ticket 09 (PML write/read mechanism), ticket 10 (interface shape — `createOperation`/`createObligation` dispatch must exist to call into the store correctly).

## Out of scope for this ticket

`getOperation`/`getOperations` read-path assembly logic in `OperationsQuerier` (ticket 13) — this ticket is the Neo4j store's own row read/write, not the querier-level registry-vs-store branching.

## Comments

Landed together with ticket 13 — the store's own row read/write and the querier-level assembly logic that
consumes it are tightly coupled (the store can't be tested meaningfully without a caller that branches on
`operation_kind`), same rationale ticket 10+11 used to combine.

- `OPERATION_LABEL` renamed `"AdminOp"` → `"Operation"`. New `OperationKind` enum (`pap/operation/OperationKind.java`, `NATIVE`/`PML`) and new `Neo4jUtil` properties `OPERATION_KIND_PROPERTY`, `PML_TEXT_PROPERTY`, `AUTHOR_ID_PROPERTY`, `AUTHOR_NAME_PROPERTY`, `AUTHOR_PROCESS_PROPERTY`.
- `Neo4jEmbeddedOperationsStore`/`Neo4jEmbeddedObligationStore` rewired off `Neo4jUtil.serialize`/`deserialize` entirely: write calls `toString()`/`toFormattedString(0)` and stores `pml_text` (PML-kind rows only; native rows are `name` + `operation_kind`, no body); read calls `StatementVisitor.fromString(pap, pmlText)` and pulls the live object back out via `OperationDefinitionStatement.getOperation()` / `CreateObligationStatement.toObligation(author)`.
- Obligation author persisted as `author_id`/`author_name`/`author_process`, written only when the corresponding `NodeUserContext` field is non-null/non-sentinel; read path picks the matching `NodeUserContext.of(...)` overload.
- `Neo4jUtil.serialize`/`deserialize` deleted, along with `Neo4jUtilTest` (its sole subject). `ClassLoader` threading (`Neo4jEmbeddedPolicyStore`/`Neo4jEmbeddedOperationsStore`/`Neo4jEmbeddedObligationStore` constructors) removed entirely — it existed only to support `Class.forName` during deserialization, which no longer exists. One test call site (`Neo4JEmbeddedPAPTest.init`) updated.
- **New wiring not anticipated by the design doc**: the read path needs a `PAP` to lazily resolve cross-references via `StatementVisitor.fromString`, but `PolicyStore` is constructed before the `PAP` wrapping it exists. Added `PolicyStore.setPap(PAP pap)` (default no-op), called once from `PAP`'s constructor (and `withPolicyStore`) before the store is used for anything. `Neo4jEmbeddedPolicyStore` overrides it and threads the reference into each `Neo4jEmbeddedOperationsStore`/`Neo4jEmbeddedObligationStore` it hands out; `MemoryPolicyStore` doesn't need it (pure passthrough, per ticket 09).
- **Bug caught by the Neo4j-backend test suite, not by inspection**: a `Node`'s properties can't be read after the `TxHandler.runTx` lambda that fetched it returns (`NotInTransactionException`) — fixed by reading every property needed (kind, PML text, author fields) into a local record *inside* the transaction lambda, never touching the `Node` reference afterward. Caught three call sites doing this wrong on first pass (`getOperation`, `getOperationKind`, and the analogous obligation read); the Memory-backend tests couldn't have caught it since Memory never touches a `Node`.
- Confirmed `operation_name_index` (declared against label `Operation`, previously silently inert against `"AdminOp"`) now applies for free via the label rename — no new index needed. Verified with a `PROFILE`-based test (`Neo4jOperationNameIndexTest`), not just asserted: a name lookup on label `Operation` no longer plans as `NodeByLabelScan`.
- Verified: `ObjectOutputStream`/`ObjectInputStream`/`writeObject`/`readObject` have zero remaining callers in `src/main` after this ticket (re-confirmed for ticket 14 too).
