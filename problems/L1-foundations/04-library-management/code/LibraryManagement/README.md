# Library Management System

A low-level design implementation of a library catalogue, circulation (borrow/return),
reservations, and fines — built incrementally against the requirements in
[`../REQUIREMENTS.md`](../REQUIREMENTS.md).

## Package layout

```
src/main/java/com/shubham/libraryManagement/
├── entities/
│   ├── Book.java          # catalogue entry (title/author/subject/ISBN) — a "work", not a copy
│   ├── BookItem.java       # one physical copy of a Book: barcode, status, current holder
│   ├── Member.java         # a library member: borrowed copies, fine balance
│   ├── Library.java        # the aggregate root — catalogue, inventory, circulation, reservations
│   └── Fine.java           # Strategy interface for fine calculation
├── enums/
│   ├── BookStatus.java      # AVAILABLE | BORROWED | RESERVED
│   ├── MemberStatus.java    # ACTIVE | BLOCK
│   └── MemberType.java      # NORMAL | HIGH_VALUE_CUSTOMER
└── service/
    ├── FineFactory.java                 # picks a Fine strategy by MemberType
    ├── NormalValueTypeFineService.java  # Rs 10/day, capped at Rs 10
    ├── HighValueCustomerFineService.java # Rs 10/day, capped at Rs 20
    ├── Notifier.java                     # callback for "your reservation is ready"
    └── ConsoleNotifier.java              # default Notifier implementation
```

## Core design decision: Book vs BookItem

`Book` is the *catalogue entry* — one record per title/edition (title, author, subject, ISBN).
`BookItem` is one *physical copy* of a `Book` — it has its own barcode and a mutable lifecycle
(`AVAILABLE → BORROWED → AVAILABLE`, or `AVAILABLE → RESERVED → BORROWED`). Search operates on
`Book`; borrowing, returning, and reserving operate on (or resolve to) a specific `BookItem`.
`Library` holds the mapping from a catalogue ISBN to its list of physical copies.

## How each requirement is implemented

| Requirement | Implementation |
|---|---|
| Catalogue + multiple copies per title | `Library.addBook` / `addBookItem`; copies tracked in `bookItemsByISBN` |
| Search by title/author/subject/ISBN, fast at scale | Inverted index: `booksByTitle` / `booksByAuthor` / `booksBySubject` map lowercased **tokens** → ISBNs; `booksByISBN` gives O(1) exact ISBN lookup. All lookups are O(1) average regardless of catalogue size, which is how `searchBook` stays fast at 1M items. |
| Borrow with a loan limit and fixed period | `Library.borrowCopy`; `MAX_ACTIVE_LOANS = 5`, `LOAN_PERIOD_DAYS = 14` |
| Return + fine for overdue days | `Library.returnBook` computes `overdueDays = max(0, daysBorrowed - 14)` and delegates the Rs amount to a `Fine` strategy |
| Fine policy configurable per membership type | **Strategy pattern**: `Fine` interface, `FineFactory.getFineService(MemberType)`, two implementations with different caps |
| Reserve when unavailable, notify first reserver on return | `Library.reserveBook` enqueues onto a per-ISBN `Queue<Member>`; `returnBook` calls `promoteNextReservation`, which holds the returned copy (`BookStatus.RESERVED`) for the next eligible member and calls `Notifier.notifyAvailable` |
| Member with unpaid fines can't borrow | `Member.outstandingFine`, charged in `returnBook` via `addFine`; `validateMember` rejects borrowing while the balance is > 0; `Library.payFine` clears it |
| Librarians add/remove books, block members | `addBook`/`removeBook`, `blockMember`/`unblockMember` |
| Borrow/return are atomic (no double-borrow of one copy) | Each `BookItem` is locked with `synchronized (bookItem)` around its status check-and-mutate in both `borrowCopy` and `returnBook` — fine-grained, so unrelated copies/books never contend with each other |

## Design patterns used

- **Strategy** — `Fine` / `FineFactory`, so fine calculation varies by `MemberType` without an `if/else` in `Library`.
- **Observer-ish callback** — `Notifier`, injected into `Library`, called when a reservation becomes ready. (It's a single injected callback rather than a true multi-subscriber Observer — sufficient for this scope, noted as a simplification.)
- **Inverted index** — the search maps are a minimal hand-rolled version of what Lucene/Elasticsearch do internally: trade a bit of write-time bookkeeping (`addBook`/`removeBook` update four maps) for O(1) read-time search regardless of catalogue size.

## Concurrency

- `bookItemsByISBN` is a `ConcurrentHashMap` of `CopyOnWriteArrayList` — safe for concurrent `addBookItem` writes and concurrent `borrowCopy` scans (reads are far more frequent than writes, which is the case `CopyOnWriteArrayList` is built for).
- Each `BookItem`'s status transition is guarded by `synchronized (bookItem)` in both `borrowCopy` and `returnBook`, so two members racing for the same copy can't both win, and a copy being returned transitions `BORROWED → RESERVED` atomically (a concurrent `borrowCopy` call never observes it as momentarily `AVAILABLE`).
- `Member`'s fine balance methods (`addFine`/`payFine`/`getOutstandingFine`) are `synchronized` on the member instance.
- **Known gap**: `Member.countOfBorrowedCopies()` (used by the 5-loan cap) reads a plain `HashMap` with no lock, so a member borrowing concurrently from two threads could theoretically exceed the loan limit. Left open — see below.

## Known gaps / not yet implemented

These were identified against `../REQUIREMENTS.md` and deliberately deferred:

- **Renew a loan** (FR6) — no `renewLoan` method yet.
- **Reservation expiry** (EC3, "reserved copy not collected within 3 days") — `BookItem.reservedTime` is stamped but nothing ever checks it or releases the hold to the next reserver.
- **Copy reported lost** (EC4) — no `LOST` value in `BookStatus`, no `reportLost` method.
- **Repository abstraction** — `Library` directly owns and mutates raw `Map`s for everything (catalogue, inventory, members, reservations) instead of delegating to `BookRepository`/`MemberRepository`/`BookItemRepository` interfaces. This is the main structural difference from the "Repository" pattern the requirements call out.
- **Typed results instead of exceptions** — the spec's `checkout(memberId, barcode) -> Loan | {LimitReached, HasFines, Reserved, NotAvailable}` wants a typed result; the current code throws `IllegalArgumentException` with a descriptive message for all rejection cases, which works but doesn't let callers pattern-match on the reason.
- **No `Loan` entity** — loan state (who, since when) lives directly on `BookItem` and is mirrored in `Member.borrowedCopies`, rather than as a first-class aggregate with its own id/due date/renewal count.
- **Member-level loan-count race** (above).

## Running it

```bash
./gradlew compileJava
./gradlew test
```

## Tests

| File | Covers |
|---|---|
| `LibraryConcurrencyTest` | 50 members racing for one copy — exactly one borrow succeeds |
| `LibraryFineAndReturnTest` | No fine inside the loan period; fine caps for normal vs. high-value members; a returned copy becomes borrowable again; the 6th active loan is rejected |
| `LibraryReservationTest` | Reserving while a copy is available is rejected; first reserver is notified and can claim the held copy while others can't; the next reserver is promoted after the first claims and returns |
| `LibraryFineBalanceTest` | Borrowing is blocked with an unpaid fine; full payment restores access; partial payment still blocks; overpayment is rejected |
