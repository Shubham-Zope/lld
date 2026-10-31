# How to practise LLD

## The 45-minute interview script

| Minute | Phase | What you produce | Signals the interviewer looks for |
|---|---|---|---|
| 0–5 | Clarify | Scope list: in, out, assumptions | Asks about actors, scale, concurrency, extensibility; does not start coding blind |
| 5–10 | Entities | Nouns → classes, verbs → methods, enums for closed sets | Correct ownership; Book vs BookItem style distinctions |
| 10–20 | Design | Class diagram, relationships, interfaces, patterns | Each pattern tied to a requirement; interfaces at volatile points |
| 20–38 | Code | Core classes and main flow | Readable, cohesive, encapsulated, no god class |
| 38–45 | Extend & defend | Answer a follow-up, discuss concurrency and trade-offs | Change is local; honest trade-offs |

## Questions to ask on every problem

1. Who are the actors and what can each one do?
2. What is in scope for v1, and what is explicitly out?
3. Single process or many? How many concurrent users act on the same object?
4. What changes most often (pricing, rules, channels)? That is where interfaces go.
5. What must never happen (double booking, negative balance)? Those are your invariants.
6. Is persistence required or is in-memory with a repository interface enough?

## Red flags that cost points

- One `Manager`/`System` class that does everything
- `if (type == ...)` chains where polymorphism belongs
- Public mutable fields, setters for everything
- Patterns named but not needed, or needed but missing
- `double` for money; `LocalDateTime.now()` / `System.currentTimeMillis()` hard-coded inside logic instead of an injected `java.time.Clock`
- No word on concurrency for a shared resource

## Level progression rule

Move to the next level when your last three mocks in the current level score 75+ and you can redo any problem's design from scratch in 15 minutes.
