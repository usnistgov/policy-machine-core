# Changelog

## [3.3.0] - 2026-04-22

### Breaking Changes

- **Annotation renames**: PML operation annotations are now PascalCase to match Java convention.
    - `@node` → `@Node`
    - `@reqcap` → `@ReqCap`
    - `@eventctx` → `@EventCtx`
- **`UserContext` and `TargetContext` refactored** (#230): These types have been significantly restructured. Any code depending on their internal API will need to be updated.
- **Association statement syntax change** (#221): The `and` keyword in association statements has been replaced with `to` (e.g., `associate "ua" and "oa"` → `associate "ua" to "oa"`).
- **Operations and prohibitions refactored** (#215): Major restructuring of how operations and prohibitions are defined and stored. See updated [PML docs](docs/pml.md) for current syntax.
- **`@ReqCap` refactored** (#220): Required capability annotation semantics and structure updated.
- **Resource event emit removed from PDP** (#217): Resource operations no longer automatically emit events through the PDP; event context is now controlled via `@EventCtx`.
- **`AdminFunction` args refactored** (#210): Internal argument handling for admin operations has changed.
- **Functions persisted as operations** (#205): Functions are now stored as first-class operations rather than a separate construct.

### New Features

- **Optional parameters and named args in PML** (#229): Operation parameters can now be marked optional with `?`, and call sites can pass named arguments.
- **Custom event context args via `@EventCtx`** (#223): Operations can declare which parameters appear in the event context sent to the EPP, independent of the operation signature.
- **`if exists` clause in delete statements** (#203): PML `delete` statements now support `if exists` to avoid errors when deleting non-existent nodes.
- **Self-access moved to PAP** (#225): Self-access privilege checking is now handled at the PAP layer.
- **Resource operation wrapper** (#219): Added a wrapper class for resource operations to simplify common patterns.
- **gRPC helper classes** (#222, #218): Improved helper classes and client stubs for gRPC-based integrations.

### Bug Fixes

- Prevent `process` from being `null` in event context.
- Fix `toFormattedString` for subject pattern in obligation statements.
- Fix `performs assign` syntax in PML docs.
- Fix prohibition creation syntax in PML docs.
- Fix Neo4j operation serialization and obligation execution (#206).
- Fix delete statement `toString` with `if exists` (#204).
- Fix JSON schema and serializer (#202).
- Fix `process` value in event context (#200).

### Improvements

- `@Node` annotation now only enforces node parameters as required where applicable (#228).
- Obligation statements now allow expressions in the subject pattern (#226, #227).
- Plugins are now ignored during bootstrapping (#208).
- Refactored plugin functions and various small fixes (#207).
- Removed `maven-assembly-plugin` from build (#216).
- Refactored internal annotation handling in PML operations (#224).

---

## [3.1.0] and earlier

See [GitHub releases](https://github.com/PM-Master/policy-machine-core/releases) for prior release notes.