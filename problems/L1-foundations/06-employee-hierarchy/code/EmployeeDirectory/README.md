# Employee Directory & Org Chart (Java 25 + Gradle)

An interview-oriented, runnable in-memory LLD implementation of an employee directory and
reporting hierarchy, with HR write operations and read-heavy org-chart queries.

## Run in IntelliJ IDEA

1. Open this `EmployeeDirectory` folder in IntelliJ as a project and trust it.
2. Let IntelliJ import/sync Gradle (toolchain requests JDK 25).
3. Open `src/main/java/com/shubham/employeedirectory/demo/Main.java` and run `main` with the
   green run icon — it builds a sample org chart and walks through every requirement and edge case.
4. Run the tests from the Gradle tool window, or:

```bash
./gradlew test
```

## Implemented features

- `addEmployee` with manager, department (optional), role and location; the first employee added
  with no manager becomes the CEO, and a second one is rejected (single-CEO invariant).
- `moveEmployee(id, newManagerId, withSubtree)`:
  - `withSubtree = true` — the employee and their whole team move to the new manager.
  - `withSubtree = false` — only the employee moves; their direct reports are promoted to the
    employee's old manager, the same reassignment `removeEmployee` performs.
  - Rejects the move if the new manager is the employee themself or already reports to them
    (directly or indirectly) — no cycles, ever.
  - Rejects moving the CEO (they have no manager to move under).
  - Rejects the move if it would push any report past the 20-level depth cap.
- `removeEmployee` reassigns the removed employee's direct reports to the removed employee's own
  manager; removing the CEO is rejected.
- Queries: direct reports, full subtree, chain up to the CEO, lowest common manager of two
  employees, headcount by department, headcount by location, and a department headcount/printable
  chart scoped to any employee's subtree.
- Every query reflects the latest writes immediately — there is no cache to invalidate because the
  tree and the headcount maps are updated in place on every write.

## Design map

- **Composite** — `Employee` is the tree node: every employee uniformly holds a `manager`
  reference and a set of `directReports` of the same type, so a leaf (no reports) and a manager
  (many reports) are handled identically by every traversal.
- **Visitor** (`EmployeeVisitor`, `visitor/`) — reports over the Composite tree without adding
  methods to `Employee` for each report. `DepartmentHeadcountVisitor` tallies a subtree's headcount
  by department; `OrgChartPrinterVisitor` renders an indented chart. Adding a new report (e.g. a
  location breakdown of a subtree) means adding one new visitor class, not touching `Employee`.
- **Iterator** (`SubtreeIterator`) — a non-recursive, breadth-first `Iterator<Employee>` over an
  employee and all of their reports, exposed as `Employee.subtree()`. Used by `getSubtree` and kept
  separate from the Visitor so a caller can also stream a huge subtree lazily instead of visiting
  it eagerly.
- **Aggregate root / facade** — `EmployeeDirectoryService` owns the id index, the single-CEO
  invariant, the no-cycle check, the 20-level depth cap, and the cached department/location
  headcount maps (incremented or decremented on every add/remove instead of recomputed).
  It is the *only* class that can restructure the tree: `Employee`'s sole structural mutator,
  `reparentTo`, is package-private, and `EmployeeDirectoryService` lives in the same `model`
  package specifically so it is the only other class with access. `reparentTo` also updates
  `manager` and `directReports` together in one call, so the two sides can never go out of sync
  — not even from a bug inside the service itself. A caller that gets a live `Employee` back from
  `getEmployee`/`getDirectReports`/etc. is handed a read-only view in practice: there is no public
  method on it that changes the tree.

## Complexity (why this is fine at 100k employees)

The tree has depth capped at 20, so everything that walks "up" (cycle check, chain to CEO, lowest
common manager) is **O(depth) ≤ 20**, independent of company size:

| Operation | Cost |
|---|---|
| `addEmployee` | O(depth) — depth check walks up to the manager |
| `moveEmployee` | O(depth) for the cycle/depth checks; `withSubtree=true` additionally pays O(height of the moved subtree) to check the depth cap; O(k) to reassign k direct reports when `withSubtree=false` |
| `removeEmployee` | O(k), k = the removed employee's direct reports |
| `getDirectReports` | O(k) |
| `getSubtree` | O(size of that subtree) |
| `getChainToCeo` | O(depth) |
| `lowestCommonManager` | O(depth) — walk both manager chains to the CEO, no subtree scan |
| `headcountByDepartment` / `headcountByLocation` | O(distinct departments/locations) — cached counters, not a full scan |

## Edge cases covered

- Moving a manager under their own (direct or indirect) report → `CycleException`.
- Moving or removing the CEO → `InvalidOperationException`.
- Adding or moving an employee such that they (or, when moving a subtree, any of their reports)
  would land past depth 20 → `InvalidOperationException`.
- An employee with no department (`department == null`) is excluded from department headcount but
  still counted by location.
- Adding a duplicate employee id → `DuplicateEmployeeException`.
- An unknown manager or employee id on any operation → `EmployeeNotFoundException`.

## Not implemented (by design)

This is a single-threaded, in-memory model. It doesn't cover:

- **Concurrency.** Concurrent writers would need a lock (or a persistent/immutable tree) around the
  mutating operations in `EmployeeDirectoryService`; reads would need a consistent snapshot.
- **Dotted-line (matrix) reporting.** Add it as a separate `Set<Employee> dottedLineManagers` on
  `Employee` (or a standalone many-to-many index in the service) alongside the solid-line `manager`
  — the Composite tree, cycle check and headcount logic for the solid line stay untouched.
  Subtree/visitor queries would take a flag for whether to follow dotted lines too.
- **Historical org chart as of a date.** Requires recording each write as a timestamped event
  (hire, move, remove) rather than mutating in place, then replaying events up to a given date to
  reconstruct that day's tree — an append-only event log behind the same `EmployeeDirectoryService`
  API, rather than a change to `Employee` or the queries themselves.

## Extend

To add a new report over the org chart (e.g. "average tenure per department" for a subtree),
implement `EmployeeVisitor` and pass it to `Employee.accept(...)`; no existing class needs to change.
