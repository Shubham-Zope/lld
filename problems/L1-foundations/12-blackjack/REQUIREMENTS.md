# 12 · Card Game Engine (Blackjack) — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Strategy (dealer rules), State (round), Factory

## Problem statement

Design a reusable deck-of-cards library and a Blackjack game on top of it: shoe of multiple decks, players with chips, dealer rules, hit/stand/double/split, and payouts. The card library should be reusable for other games.

## Functional requirements

- [ ] **FR1** Card, Suit, Rank, Deck, Shoe (N decks) with shuffle
- [ ] **FR2** Player hands, bets, chips
- [ ] **FR3** Actions: hit, stand, double, split
- [ ] **FR4** Dealer hits until 17 (soft-17 rule configurable)
- [ ] **FR5** Ace counts 1 or 11
- [ ] **FR6** Payouts: blackjack 3:2, win 1:1, push

## Non-functional requirements

- [ ] **NFR1** Card library independent of Blackjack
- [ ] **NFR2** Shuffle injectable

## Constraints & assumptions

- 1-7 players, 1-8 decks

## Edge cases to handle

- [ ] **EC1** Split aces
- [ ] **EC2** Shoe runs low mid-round
- [ ] **EC3** Bet exceeds chips
- [ ] **EC4** Dealer and player both blackjack

## Expected behaviour

- Round produces settlement per hand

## Design concepts this problem exercises

- Reusable domain library
- Hand value with soft totals
- Rules configuration

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Splitting? | Yes, once. |
| Soft 17? | Configurable. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Insurance
- Poker using same card library

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.
