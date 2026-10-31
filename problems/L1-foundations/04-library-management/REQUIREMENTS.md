# 04 · Library Management System — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 45 min
> **Patterns usually relevant:** Observer, Strategy, Repository

## Problem statement

Design a system for a public library to manage its catalogue, members, borrowing, returns, reservations and fines. A title can have many physical copies. Members borrow copies, can reserve titles that are all checked out, and are fined for late returns. Librarians manage the catalogue.

## Functional requirements

- [ ] **FR1** Catalogue of books (title, authors, ISBN, subject) with multiple BookItems (copies) each with a barcode and status
- [ ] **FR2** Search by title, author, subject or ISBN
- [ ] **FR3** Members borrow up to a limit of copies for a fixed period
- [ ] **FR4** Return a copy; compute fine for overdue days
- [ ] **FR5** Reserve a title when no copy is available; notify the first reserver when a copy returns
- [ ] **FR6** Renew a loan if no one has reserved the title
- [ ] **FR7** Librarians add/remove books and block members

## Non-functional requirements

- [ ] **NFR1** Search response under 200 ms for 1M items (indexing)
- [ ] **NFR2** Borrow/return operations are atomic
- [ ] **NFR3** Fine policy configurable per membership type

## Constraints & assumptions

- Up to 1M copies, 100k members
- Max 5 active loans per member, 14-day loan by default
- Fine Rs 10/day, capped

## Edge cases to handle

- [ ] **EC1** Member with unpaid fines tries to borrow
- [ ] **EC2** Returning a copy that was reserved by someone else
- [ ] **EC3** Reserved copy not collected within 3 days
- [ ] **EC4** Copy reported lost
- [ ] **EC5** Renewal requested on an overdue loan

## Expected behaviour

- checkout(memberId, barcode) returns Loan or a typed error (LimitReached, HasFines, Reserved, NotAvailable)
- return(barcode) returns Fine (possibly zero) and triggers reservation notification

## Design concepts this problem exercises

- Book vs BookItem (type vs instance)
- Aggregates
- Observer for notifications
- Strategy for fine calculation
- Repository abstraction

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Multiple copies per book? | Yes, each copy is tracked separately. |
| Do we handle reservations? | Yes, a FIFO queue per title. |
| Fines? | Per day overdue, policy differs by membership type. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- E-books with concurrent licence limits
- Inter-branch transfers
- Recommendation of similar titles

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.
