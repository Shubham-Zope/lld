# 05 · Shopping Cart & Checkout

`L1 · Foundations` · `Core` · `Easy` · `45 min` · Patterns: `Strategy`, `Chain of Responsibility`, `Composite (bundled products)`, `Factory`

Design the cart and checkout module of an e-commerce store. Customers add products with variants to a cart, apply coupons, see a price breakdown with taxes and shipping, and check out into an order. Discounts and tax rules change frequently and must be pluggable.

## Files

| File | What goes in it |
|---|---|
| [REQUIREMENTS.md](./REQUIREMENTS.md) | Full spec: FR, NFR, constraints, edge cases, expected behaviour |
| [DESIGN.md](./DESIGN.md) | Your class diagram, patterns, SOLID check, trade-offs |
| `src/main/java/com/lld/shoppingcart/` | Implementation |
| `src/test/java/com/lld/shoppingcart/` | JUnit 5 tests, one per FR and edge case at minimum |

## Status

- [ ] Attempt 1 — timed (45 min), no references
- [ ] Self-review against `docs/evaluation-rubric.md`
- [ ] Mock interview in the practice tool — score: __/100
- [ ] Refactor based on feedback
- [ ] Revision (after 7 days) — redo design only in 15 min
- [ ] Revision (after 30 days)

## My key learnings

-

## Run

```bash
./gradlew :shopping-cart:test
```
