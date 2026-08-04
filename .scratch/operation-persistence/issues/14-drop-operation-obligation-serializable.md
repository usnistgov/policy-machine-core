Status: resolved

## Summary

Drop `implements Serializable` from `Operation`/`Obligation` per [design doc §7](../../../docs/operation-persistence-design.md#7-drop-serializable).

## Scope

- Remove `implements Serializable` from `Operation` (`pap/operation/Operation.java`) and `Obligation` (`pap/obligation/Obligation.java`).
- Remove `Operation`'s now-dead `serialVersionUID = 1L`.
- Do **not** cascade to field types (`RequiredCapability`, `RequiredPrivilege`, `Type`, `FormalParameter`, `EventPattern`, `ObligationResponse`) — leave their `Serializable` declarations untouched, out of scope.
- Do **not** touch `PMLStatementSerializable` or its implementors (`SubjectPattern`, `OperationPattern`, `PMLStmts*Operation` classes) — unrelated, consumed by `JSONSerializer` for policy JSON export/import.
- Grep `src/main` to reconfirm `java.io.ObjectOutputStream`/`ObjectInputStream`/`writeObject`/`readObject` have zero remaining callers after ticket 12 deletes `Neo4jUtil.serialize`/`deserialize` — this ticket should be the one to actually verify that, not just assume the design doc's planning-time grep still holds.
- Add a compatibility note to the PR description (mirroring the ADR's Consequences section): breaking change for any embedding code relying on `Operation`/`Obligation` being `java.io.Serializable` for unrelated purposes; no migration shim.

## Depends on

Ticket 08, ticket 09 (both types need their persistence mechanism landed first, though this ticket itself is a small isolated change), and ticket 12 (must land first — `Neo4jUtil.serialize`/`deserialize` deletion is the thing that makes dropping `Serializable` safe).

## Out of scope for this ticket

Cascading removal to field types — that's explicitly out of scope for the whole map (see design doc §9), tracked as a separate future cleanup, not a ticket here.

## Comments

Landed after ticket 12 (which deleted `Neo4jUtil.serialize`/`deserialize`, the last thing that needed the marker).

- `implements Serializable` and `serialVersionUID` removed from `Operation`; `implements Serializable` removed from `Obligation`. Field types (`RequiredCapability`, `RequiredPrivilege`, `Type`, `FormalParameter`, `EventPattern`, `ObligationResponse`) untouched, per scope.
- Re-grepped `src/main` for `ObjectOutputStream`/`ObjectInputStream`/`writeObject`/`readObject`: zero matches, confirming ticket 12's deletion left nothing else depending on the marker.
- **Compatibility note for the PR description**: breaking change for any embedding/downstream code relying on `Operation`/`Obligation` implementing `java.io.Serializable` for unrelated purposes (session storage, distributed caching, in-process queues that Java-serialize payloads). No migration shim, consistent with this effort's existing precedent (ADR 0001).
- Full suite (`mvn -o test`): 948/948 passing.
