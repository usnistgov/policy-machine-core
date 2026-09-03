## Agent skills

### Issue tracker

Issues and specs live as markdown files under `.scratch/<feature-slug>/`. See `.scratch/docs/agents/issue-tracker.md`.

### Domain docs

Single-context layout — `CONTEXT.md` + `docs/adr/` at the repo root. See `.scratch/docs/agents/domain.md`. Note: this repo currently keeps ADRs/design docs under `.scratch/docs/adr/` instead (untracked, local-only, by preference) rather than the tracked `docs/adr/` the file describes.

## Javadoc style

Keep Javadoc short — prefer a single plain sentence for the class/method summary. Only explain non-obvious behavior (e.g. an exception-handling quirk); don't restate what the signature already says.

Favor short, generic, plain-language phrasing over packing in every distinguishing detail — a terse one-liner beats an exhaustive one, even if that means leaving out specifics a reader can get from the code or other docs. Avoid jargon (e.g. say "task" not "unit of work").

No colons in the summary sentence — if the old style was "X: does Y", write it as two short sentences or reword as one.

Use `{@link}` sparingly, only for the primary type being described (e.g. "A {@link Foo} that..."), not stitched into the middle of a sentence for every related type. Never use `{@code}`.
