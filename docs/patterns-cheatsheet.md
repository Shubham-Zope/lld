# Patterns cheat sheet: requirement → pattern

| When the requirement says… | Consider | Seen in |
|---|---|---|
| Behaviour depends on current state; transitions are rules | State | Vending Machine, ATM, Elevator, Order lifecycle |
| Algorithm or policy must be swappable | Strategy | Parking allocation, pricing, eviction, matching |
| Many parties react to a change | Observer / domain events | Display boards, notifications, projections |
| A request passes through ordered handlers | Chain of Responsibility | Logger, ATM dispenser, middleware |
| Add behaviour by wrapping | Decorator | Coffee add-ons, retry/metrics wrappers |
| Part-whole trees treated uniformly | Composite | File system, org chart, rule conditions |
| Construct complex objects step by step | Builder | Board config, workflow DSL |
| Create one of many types by input | Factory | Vehicles, notification channels |
| Undo / queue / log actions | Command | Tic-Tac-Toe undo, KV transactions |
| Evaluate user-defined expressions | Interpreter / Specification | Rule engine, cron, search filters |
| Snapshot and restore state | Memento | Chess, event-sourcing snapshots |
| Control access or add caching transparently | Proxy | Connection pool, policy cache |
| Reuse expensive objects | Object Pool | Connection pool, thread pool |

Use a pattern only when you can name the requirement that forces it.
