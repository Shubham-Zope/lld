# 16 · URL Shortener (Object Model) — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 35 min
> **Patterns usually relevant:** Strategy, Repository, Factory

## Problem statement

Design the object model and core logic of a URL shortener: create short codes for long URLs, custom aliases, expiry, redirect lookup and click analytics. Focus on the class design and code generation strategy, not distributed storage.

## Functional requirements

- [ ] **FR1** Shorten(url, alias?, expiry?)
- [ ] **FR2** Resolve(code) returns long URL or not found/expired
- [ ] **FR3** Custom aliases with validation
- [ ] **FR4** Click counting by day and referrer
- [ ] **FR5** Delete/disable link by owner

## Non-functional requirements

- [ ] **NFR1** Code generation strategy pluggable (base62 counter, hash, random)
- [ ] **NFR2** Lookup O(1)
- [ ] **NFR3** Repository abstraction for storage

## Constraints & assumptions

- Codes 7 characters base62

## Edge cases to handle

- [ ] **EC1** Alias collision
- [ ] **EC2** Same long URL shortened twice
- [ ] **EC3** Malicious or invalid URL
- [ ] **EC4** Expired link

## Expected behaviour

- resolve never returns expired links

## Design concepts this problem exercises

- Strategy for ID generation
- Repository
- Validation

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Same URL twice gets same code? | Per user, yes. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Rate limit per user
- Link previews

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.
