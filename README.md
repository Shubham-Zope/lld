<div align="center">

# LLD Mastery

**Low-level design practice from beginner to Principal Engineer**

100 problems · 6 levels · full specs · design worksheets · implementations · mock-interview scorecards

![Problems](https://img.shields.io/badge/problems-100-1f6b5a)
![Core](https://img.shields.io/badge/core-41-2f7a3c)
![Extended](https://img.shields.io/badge/extended-59-7c848a)
![Solved](https://img.shields.io/badge/solved-0%2F100-a2620d)
![Language](https://img.shields.io/badge/language-TypeScript-3178c6)

</div>

---

## Why this repo exists

Low-level design rounds test whether you can turn a vague prompt into a clean, extensible object model under time pressure. Reading about patterns doesn't build that skill; repetition against real interview prompts does.

This repo is a structured practice log. Every problem has a written specification (functional and non-functional requirements, constraints, edge cases, expected behaviour), a design worksheet, an implementation with tests, and the feedback from a timed mock interview. Problems are ordered so that each level builds on the concepts of the one before.

## Contents

- [Levels at a glance](#levels-at-a-glance)
- [How I practise each problem](#how-i-practise-each-problem)
- [Roadmap](#roadmap)
- [Browse by design pattern](#browse-by-design-pattern)
- [Evaluation rubric](#evaluation-rubric)
- [Repository structure](#repository-structure)
- [Running the code](#running-the-code)
- [Conventions](#conventions)
- [References](#references)

## Levels at a glance

| Level | Theme | Target role | Core | Extended | Progress |
|---|---|---|:-:|:-:|:-:|
| [L1](#l1--foundations) | Foundations | Junior / SDE1 | 6 | 10 | 0/16 |
| [L2](#l2--design-patterns) | Design Patterns | SDE1 → SDE2 | 8 | 10 | 0/18 |
| [L3](#l3--real-world-systems) | Real-World Systems | SDE2 | 8 | 14 | 0/22 |
| [L4](#l4--concurrency--infrastructure-components) | Concurrency & Infrastructure Components | SDE2 → SDE3 | 8 | 10 | 0/18 |
| [L5](#l5--staff-level-domains) | Staff-Level Domains | SDE3 / Staff | 6 | 8 | 0/14 |
| [L6](#l6--principal-level-frameworks) | Principal-Level Frameworks | Principal Engineer | 5 | 7 | 0/12 |
| | | **Total** | **41** | **59** | **0/100** |

**Two tracks**

- ⭐ **Core (41)**: the problems that come up most in LLD and machine-coding rounds. Each gets a full implementation, tests and two spaced revisions.
- **Extended (59)**: breadth. Design-first (clarify, design worksheet, mock interview), with full implementation for domains relevant to target companies.

**Promotion rule:** move to the next level after three Core mock interviews in a row meet that level's pass mark.

## How I practise each problem

| Step | Time | Output |
|---|:-:|---|
| 1. Read only the problem statement | 1 min | — |
| 2. Clarify scope, actors, scale, invariants, change axes | 5 min | `DESIGN.md` §1 |
| 3. Model entities, relationships and interfaces | 15 min | Class diagram, pattern choices |
| 4. Implement the core classes and main flows | 20 min | `src/` |
| 5. Extend with one follow-up and note what changed | 5 min | `DESIGN.md` §9 |
| 6. Mock interview and scored debrief | — | `FEEDBACK.md` |
| 7. Refactor, add tests for every FR and edge case | — | `tests/` |
| 8. Revise at +7 and +30 days: redesign from a blank page in 15 min | 15 min | Updated status |

## Roadmap

Status key: ⬜ not started · 🟨 designed · 🟩 implemented and tested · ✅ revised

### L1 · Foundations

`Junior / SDE1` · Weeks 1-2 (Core)

Classes, objects, encapsulation, composition over inheritance, clean interfaces, enums, basic validation. Model the nouns and verbs correctly before reaching for patterns.

**Exit criteria:** You can turn a vague prompt into entities, relationships and a working in-memory implementation in 45 minutes, with no god classes.

<details>
<summary><b>16 problems</b> · concepts: Encapsulation, Abstraction, Inheritance vs composition, Polymorphism, Enums for closed sets</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 01 | [Parking Lot](problems/L1-foundations/01-parking-lot) | ⭐ | Easy | Strategy, Factory, Observer | ⬜ | – |
| 02 | [Tic-Tac-Toe (N×N)](problems/L1-foundations/02-tic-tac-toe) | ⭐ | Easy | Strategy, Command, Factory | ⬜ | – |
| 03 | [Snake and Ladder](problems/L1-foundations/03-snake-and-ladder) | ⭐ | Easy | Strategy, Builder, Observer | ⬜ | – |
| 04 | [Library Management System](problems/L1-foundations/04-library-management) | ⭐ | Easy | Observer, Strategy, Repository | ⬜ | – |
| 05 | [Shopping Cart & Checkout](problems/L1-foundations/05-shopping-cart) | ⭐ | Easy | Strategy, Chain of Responsibility, Composite | ⬜ | – |
| 06 | [Employee Directory & Org Chart](problems/L1-foundations/06-employee-hierarchy) | ⭐ | Easy | Composite, Visitor, Iterator | ⬜ | – |
| 07 | [Snake Game](problems/L1-foundations/07-snake-game) |  | Easy | Strategy, Observer, State | ⬜ | – |
| 08 | [Minesweeper](problems/L1-foundations/08-minesweeper) |  | Easy | State, Factory, Observer | ⬜ | – |
| 09 | [Course Registration System](problems/L1-foundations/09-course-registration) |  | Easy | Chain of Responsibility, Observer, Repository | ⬜ | – |
| 10 | [Car Rental System](problems/L1-foundations/10-car-rental) |  | Easy | Strategy, Decorator, State | ⬜ | – |
| 11 | [Restaurant Table Reservation](problems/L1-foundations/11-restaurant-reservation) |  | Easy | Strategy, Observer, State | ⬜ | – |
| 12 | [Card Game Engine (Blackjack)](problems/L1-foundations/12-blackjack) |  | Easy | Strategy, State, Factory | ⬜ | – |
| 13 | [Online Exam / Quiz System](problems/L1-foundations/13-online-exam) |  | Easy | Strategy, Factory, Template method | ⬜ | – |
| 14 | [Task Manager with Reminders](problems/L1-foundations/14-todo-manager) |  | Easy | Composite, Specification, Observer | ⬜ | – |
| 15 | [Gym Membership & Class Booking](problems/L1-foundations/15-gym-membership) |  | Easy | Strategy, Observer, State | ⬜ | – |
| 16 | [URL Shortener (Object Model)](problems/L1-foundations/16-url-shortener) |  | Easy | Strategy, Repository, Factory | ⬜ | – |

</details>

### L2 · Design Patterns

`SDE1 → SDE2` · Weeks 3-5 (Core)

Recognise when a problem is really a State, Strategy, Observer, Chain of Responsibility, Decorator, Composite, Factory or Builder problem, and apply the pattern without over-engineering.

**Exit criteria:** You pick a pattern because a requirement forces it, and you can name the requirement that would make you remove it.

<details>
<summary><b>18 problems</b> · concepts: Gang of Four patterns, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 17 | [Vending Machine](problems/L2-design-patterns/17-vending-machine) | ⭐ | Medium | State, Strategy, Singleton | ⬜ | – |
| 18 | [ATM System](problems/L2-design-patterns/18-atm) | ⭐ | Medium | State, Chain of Responsibility, Facade | ⬜ | – |
| 19 | [Logging Framework](problems/L2-design-patterns/19-logger-framework) | ⭐ | Medium | Chain of Responsibility, Singleton, Strategy | ⬜ | – |
| 20 | [Elevator System](problems/L2-design-patterns/20-elevator-system) | ⭐ | Medium | State, Strategy, Observer | ⬜ | – |
| 21 | [Notification Service](problems/L2-design-patterns/21-notification-service) | ⭐ | Medium | Factory, Strategy, Decorator | ⬜ | – |
| 22 | [Coffee Machine (Customisable Beverages)](problems/L2-design-patterns/22-coffee-machine) | ⭐ | Easy | Decorator, Builder, Observer | ⬜ | – |
| 23 | [In-Memory File System](problems/L2-design-patterns/23-file-system) | ⭐ | Medium | Composite, Specification, Visitor | ⬜ | – |
| 24 | [Traffic Signal Controller](problems/L2-design-patterns/24-traffic-signal) | ⭐ | Easy | State, Observer, Strategy | ⬜ | – |
| 25 | [Text Editor with Undo/Redo](problems/L2-design-patterns/25-text-editor) |  | Medium | Command, Memento, Iterator | ⬜ | – |
| 26 | [Calculator & Expression Evaluator](problems/L2-design-patterns/26-expression-evaluator) |  | Medium | Interpreter, Composite, Visitor | ⬜ | – |
| 27 | [Report Generator & Exporter](problems/L2-design-patterns/27-document-exporter) |  | Medium | Visitor, Composite, Builder | ⬜ | – |
| 28 | [Multi-Gateway Payment Adapter](problems/L2-design-patterns/28-payment-adapter) |  | Medium | Adapter, Strategy, Facade | ⬜ | – |
| 29 | [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home) |  | Medium | Command, Adapter, Observer | ⬜ | – |
| 30 | [Group Chat Application](problems/L2-design-patterns/30-chat-room) |  | Medium | Mediator, Observer, Factory | ⬜ | – |
| 31 | [Vector Shape Editor](problems/L2-design-patterns/31-shape-editor) |  | Medium | Composite, Prototype, Command | ⬜ | – |
| 32 | [Layered Configuration Loader](problems/L2-design-patterns/32-config-loader) |  | Medium | Chain of Responsibility, Adapter, Observer | ⬜ | – |
| 33 | [Music Player & Playlist Queue](problems/L2-design-patterns/33-music-player) |  | Easy | Iterator, State, Strategy | ⬜ | – |
| 34 | [Stock Price Alert Service](problems/L2-design-patterns/34-stock-alerts) |  | Easy | Observer, Strategy, Factory | ⬜ | – |

</details>

### L3 · Real-World Systems

`SDE2` · Weeks 6-9 (Core)

Multi-actor business domains with workflows, pricing, search, and the first taste of concurrency (seat locking, double booking). Layering: domain, service, repository.

**Exit criteria:** You separate domain model from services and storage, handle concurrent writes to shared resources, and keep the design open for the obvious next feature.

<details>
<summary><b>22 problems</b> · concepts: Layered architecture, Repository pattern, Service layer, Optimistic vs pessimistic locking, Idempotency basics</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 35 | [Movie Ticket Booking (BookMyShow)](problems/L3-real-world-systems/35-movie-ticket-booking) | ⭐ | Medium | State, Strategy, Observer | ⬜ | – |
| 36 | [Expense Sharing (Splitwise)](problems/L3-real-world-systems/36-splitwise) | ⭐ | Medium | Strategy, Factory, Observer | ⬜ | – |
| 37 | [Hotel Reservation System](problems/L3-real-world-systems/37-hotel-booking) | ⭐ | Medium | Strategy, State, Builder | ⬜ | – |
| 38 | [Food Delivery (Zomato/Swiggy)](problems/L3-real-world-systems/38-food-delivery) | ⭐ | Hard | State, Observer, Strategy | ⬜ | – |
| 39 | [Ride Hailing (Uber/Ola)](problems/L3-real-world-systems/39-ride-hailing) | ⭐ | Hard | State, Strategy, Observer | ⬜ | – |
| 40 | [Chess Game](problems/L3-real-world-systems/40-chess) | ⭐ | Medium | Strategy, Command, Memento | ⬜ | – |
| 41 | [Generic Cache with Pluggable Eviction (LRU/LFU)](problems/L3-real-world-systems/41-cache-eviction) | ⭐ | Medium | Strategy, Decorator, Observer | ⬜ | – |
| 42 | [Order Management System (E-commerce)](problems/L3-real-world-systems/42-order-management) | ⭐ | Hard | State, Observer, Strategy | ⬜ | – |
| 43 | [Airline Reservation System](problems/L3-real-world-systems/43-airline-booking) |  | Hard | Strategy, State, Builder | ⬜ | – |
| 44 | [Calendar & Meeting Scheduler](problems/L3-real-world-systems/44-calendar-scheduler) |  | Hard | Strategy, Observer, Iterator | ⬜ | – |
| 45 | [Project Board (Trello/Jira)](problems/L3-real-world-systems/45-kanban-board) |  | Medium | State, Observer, Command | ⬜ | – |
| 46 | [Q&A Platform (Stack Overflow)](problems/L3-real-world-systems/46-stack-overflow) |  | Medium | Observer, Strategy, Chain of Responsibility | ⬜ | – |
| 47 | [Social Media Feed](problems/L3-real-world-systems/47-social-feed) |  | Hard | Strategy, Observer, Factory | ⬜ | – |
| 48 | [Online Auction System](problems/L3-real-world-systems/48-online-auction) |  | Medium | Strategy, State, Observer | ⬜ | – |
| 49 | [Online Coding Judge (LeetCode)](problems/L3-real-world-systems/49-online-judge) |  | Hard | Strategy, State, Observer | ⬜ | – |
| 50 | [Hospital Appointment Management](problems/L3-real-world-systems/50-hospital-appointments) |  | Medium | Strategy, Observer, State | ⬜ | – |
| 51 | [Warehouse Inventory Management](problems/L3-real-world-systems/51-warehouse-inventory) |  | Hard | Strategy, Command, Observer | ⬜ | – |
| 52 | [Customer Support Ticketing System](problems/L3-real-world-systems/52-support-ticketing) |  | Medium | Strategy, State, Chain of Responsibility | ⬜ | – |
| 53 | [Music Streaming Service (Spotify)](problems/L3-real-world-systems/53-music-streaming) |  | Medium | Strategy, Observer, State | ⬜ | – |
| 54 | [Bike / Scooter Sharing](problems/L3-real-world-systems/54-bike-sharing) |  | Medium | State, Strategy, Observer | ⬜ | – |
| 55 | [Restaurant POS & Kitchen Display](problems/L3-real-world-systems/55-restaurant-pos) |  | Medium | Composite, Observer, Strategy | ⬜ | – |
| 56 | [Parcel Locker System (Amazon Locker)](problems/L3-real-world-systems/56-parcel-locker) |  | Easy | Strategy, State, Observer | ⬜ | – |

</details>

### L4 · Concurrency & Infrastructure Components

`SDE2 → SDE3` · Weeks 10-13 (Core)

Build the components other systems sit on: rate limiters, thread pools, brokers, caches, pools, circuit breakers. Thread safety, back-pressure and failure handling become primary requirements.

**Exit criteria:** You can argue the correctness of your locking strategy, explain memory/time complexity, and show how the component degrades under load.

<details>
<summary><b>18 problems</b> · concepts: Thread safety, Locks, CAS, atomics, Producer-consumer, Back-pressure, Bounded queues</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 57 | [Rate Limiter Library](problems/L4-concurrency-infrastructure-components/57-rate-limiter) | ⭐ | Medium | Strategy, Factory, Decorator | ⬜ | – |
| 58 | [Thread Pool & Task Scheduler](problems/L4-concurrency-infrastructure-components/58-thread-pool-scheduler) | ⭐ | Hard | Command, Strategy, Factory | ⬜ | – |
| 59 | [In-Memory Pub/Sub Message Broker](problems/L4-concurrency-infrastructure-components/59-pub-sub-broker) | ⭐ | Hard | Observer, Strategy, Iterator | ⬜ | – |
| 60 | [Key-Value Store with TTL & Transactions](problems/L4-concurrency-infrastructure-components/60-kv-store) | ⭐ | Hard | Command, Memento, Strategy | ⬜ | – |
| 61 | [Database Connection Pool](problems/L4-concurrency-infrastructure-components/61-connection-pool) | ⭐ | Medium | Object Pool, Factory, Proxy | ⬜ | – |
| 62 | [Circuit Breaker & Retry Library](problems/L4-concurrency-infrastructure-components/62-circuit-breaker) | ⭐ | Medium | State, Decorator, Builder | ⬜ | – |
| 63 | [Distributed-Ready Job Scheduler (Cron)](problems/L4-concurrency-infrastructure-components/63-cron-scheduler) | ⭐ | Hard | Interpreter, Strategy, Command | ⬜ | – |
| 64 | [Load Balancer with Health Checks](problems/L4-concurrency-infrastructure-components/64-load-balancer) | ⭐ | Medium | Strategy, Observer, Factory | ⬜ | – |
| 65 | [Bounded Blocking Queue](problems/L4-concurrency-infrastructure-components/65-blocking-queue) |  | Medium | Producer–Consumer, Guarded Suspension | ⬜ | – |
| 66 | [Metrics Collector (Counters, Histograms)](problems/L4-concurrency-infrastructure-components/66-metrics-collector) |  | Medium | Registry, Strategy, Observer | ⬜ | – |
| 67 | [Unique ID Generator (Snowflake)](problems/L4-concurrency-infrastructure-components/67-id-generator) |  | Medium | Singleton, Strategy | ⬜ | – |
| 68 | [Multithreaded Web Crawler](problems/L4-concurrency-infrastructure-components/68-web-crawler) |  | Hard | Producer–Consumer, Strategy, Chain of Responsibility | ⬜ | – |
| 69 | [In-Process Event Bus](problems/L4-concurrency-infrastructure-components/69-event-bus) |  | Medium | Observer, Mediator, Strategy | ⬜ | – |
| 70 | [Micro-Batching Processor](problems/L4-concurrency-infrastructure-components/70-micro-batcher) |  | Medium | Command, Strategy | ⬜ | – |
| 71 | [Service Registry & Discovery](problems/L4-concurrency-infrastructure-components/71-service-registry) |  | Hard | Observer, Proxy, Strategy | ⬜ | – |
| 72 | [Lock Manager with Leases](problems/L4-concurrency-infrastructure-components/72-distributed-lock) |  | Hard | Proxy, State | ⬜ | – |
| 73 | [Resumable Download Manager](problems/L4-concurrency-infrastructure-components/73-download-manager) |  | Medium | State, Strategy, Observer | ⬜ | – |
| 74 | [Timer Service (Hierarchical Timing Wheel)](problems/L4-concurrency-infrastructure-components/74-timing-wheel) |  | Hard | Command, Strategy | ⬜ | – |

</details>

### L5 · Staff-Level Domains

`SDE3 / Staff` · Weeks 14-17 (Core)

Money, matching engines, rule engines and workflows where correctness is non-negotiable. Idempotency, auditability, ledgers, versioning and extensibility by configuration rather than code changes.

**Exit criteria:** Your design survives retries, partial failures and new business rules without a rewrite, and you can defend every invariant.

<details>
<summary><b>14 problems</b> · concepts: Double-entry ledgers, Idempotency keys, Exactly-once effects, Rule engines, Interpreter pattern</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 75 | [Digital Wallet & Ledger](problems/L5-staff-level-domains/75-digital-wallet) | ⭐ | Hard | Command, Strategy, Template method | ⬜ | – |
| 76 | [Stock Exchange Order Matching Engine](problems/L5-staff-level-domains/76-order-matching-engine) | ⭐ | Hard | Strategy, Observer, Command | ⬜ | – |
| 77 | [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine) | ⭐ | Hard | Interpreter, Composite, Specification | ⬜ | – |
| 78 | [Configurable Workflow / State Machine Engine](problems/L5-staff-level-domains/78-workflow-engine) | ⭐ | Hard | State, Builder, Observer | ⬜ | – |
| 79 | [Feature Flag & Experimentation Service](problems/L5-staff-level-domains/79-feature-flags) | ⭐ | Medium | Strategy, Specification, Observer | ⬜ | – |
| 80 | [Search Autocomplete Engine](problems/L5-staff-level-domains/80-search-autocomplete) | ⭐ | Medium | Strategy, Decorator, Builder | ⬜ | – |
| 81 | [Payment Gateway (Merchant Side)](problems/L5-staff-level-domains/81-payment-gateway) |  | Hard | State, Adapter, Strategy | ⬜ | – |
| 82 | [Subscription Billing Engine](problems/L5-staff-level-domains/82-subscription-billing) |  | Hard | Strategy, State, Builder | ⬜ | – |
| 83 | [Loyalty & Rewards Points Engine](problems/L5-staff-level-domains/83-loyalty-points) |  | Medium | Strategy, Observer, Specification | ⬜ | – |
| 84 | [Flash Sale Inventory Reservation](problems/L5-staff-level-domains/84-flash-sale-inventory) |  | Hard | Strategy, Proxy, State | ⬜ | – |
| 85 | [Pre-Trade Risk Management System](problems/L5-staff-level-domains/85-pretrade-risk) |  | Hard | Chain of Responsibility, Strategy, Observer | ⬜ | – |
| 86 | [Marketplace Settlement & Payouts](problems/L5-staff-level-domains/86-marketplace-settlement) |  | Hard | Strategy, Command, Observer | ⬜ | – |
| 87 | [Collaborative Document Editor](problems/L5-staff-level-domains/87-collaborative-editor) |  | Hard | Command, Observer, Memento | ⬜ | – |
| 88 | [Product Catalogue Search & Facets](problems/L5-staff-level-domains/88-catalog-search) |  | Medium | Strategy, Specification, Builder | ⬜ | – |

</details>

### L6 · Principal-Level Frameworks

`Principal Engineer` · Weeks 18-20 (Core)

Design the frameworks and platforms other teams build on: DI containers, saga orchestrators, policy engines, event sourcing, middleware pipelines. API ergonomics, backward compatibility, multi-tenancy and evolution over years.

**Exit criteria:** You design for other engineers as users: stable contracts, extension points, migration paths, and clear failure semantics.

<details>
<summary><b>12 problems</b> · concepts: Framework vs library design, Inversion of Control, Plugin architectures, Backward compatibility, Event sourcing & CQRS</summary>

| # | Problem | Track | Difficulty | Key patterns | Status | Score |
|:-:|---|:-:|:-:|---|:-:|:-:|
| 89 | [Dependency Injection Container](problems/L6-principal-level-frameworks/89-di-container) | ⭐ | Hard | Factory, Registry, Proxy | ⬜ | – |
| 90 | [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator) | ⭐ | Hard | Command, State, Builder | ⬜ | – |
| 91 | [Authorization Policy Engine (RBAC + ABAC)](problems/L6-principal-level-frameworks/91-policy-engine) | ⭐ | Hard | Interpreter, Composite, Chain of Responsibility | ⬜ | – |
| 92 | [Event Sourcing & CQRS Framework](problems/L6-principal-level-frameworks/92-event-sourced-ledger) | ⭐ | Hard | Repository, Observer, Memento | ⬜ | – |
| 93 | [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline) | ⭐ | Hard | Chain of Responsibility, Decorator, Strategy | ⬜ | – |
| 94 | [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework) |  | Hard | Unit of Work, Identity Map, Proxy | ⬜ | – |
| 95 | [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host) |  | Hard | Proxy, Registry, Observer | ⬜ | – |
| 96 | [Multi-Tenant Entitlements & Plans Platform](problems/L6-principal-level-frameworks/96-entitlements-platform) |  | Hard | Strategy, Composite, Proxy | ⬜ | – |
| 97 | [Observability SDK (Tracing, Metrics, Logs)](problems/L6-principal-level-frameworks/97-observability-sdk) |  | Hard | Strategy, Decorator, Chain of Responsibility | ⬜ | – |
| 98 | [DAG Workflow Orchestrator (Airflow-like)](problems/L6-principal-level-frameworks/98-dag-orchestrator) |  | Hard | Command, Strategy, Builder | ⬜ | – |
| 99 | [Schema Registry & Compatibility Checker](problems/L6-principal-level-frameworks/99-schema-registry) |  | Hard | Strategy, Visitor, Registry | ⬜ | – |
| 100 | [Unit Testing Framework (Jest/JUnit-like)](problems/L6-principal-level-frameworks/100-test-framework) |  | Hard | Template method, Observer, Composite | ⬜ | – |

</details>

## Browse by design pattern

Patterns are tied to the requirement that forces them. This index lists the problems where each pattern is a natural fit.

<details>
<summary><b>Pattern → problems</b></summary>

| Pattern | Problems |
|---|---|
| **Strategy** (77) | [Parking Lot](problems/L1-foundations/01-parking-lot), [Tic-Tac-Toe](problems/L1-foundations/02-tic-tac-toe), [Snake and Ladder](problems/L1-foundations/03-snake-and-ladder), [Library Management System](problems/L1-foundations/04-library-management), [Shopping Cart & Checkout](problems/L1-foundations/05-shopping-cart), [Snake Game](problems/L1-foundations/07-snake-game), [Car Rental System](problems/L1-foundations/10-car-rental), [Restaurant Table Reservation](problems/L1-foundations/11-restaurant-reservation), [Card Game Engine](problems/L1-foundations/12-blackjack), [Online Exam / Quiz System](problems/L1-foundations/13-online-exam), [Task Manager with Reminders](problems/L1-foundations/14-todo-manager), [Gym Membership & Class Booking](problems/L1-foundations/15-gym-membership), [URL Shortener](problems/L1-foundations/16-url-shortener), [Vending Machine](problems/L2-design-patterns/17-vending-machine), [Logging Framework](problems/L2-design-patterns/19-logger-framework), [Elevator System](problems/L2-design-patterns/20-elevator-system), [Notification Service](problems/L2-design-patterns/21-notification-service), [Traffic Signal Controller](problems/L2-design-patterns/24-traffic-signal), [Multi-Gateway Payment Adapter](problems/L2-design-patterns/28-payment-adapter), [Group Chat Application](problems/L2-design-patterns/30-chat-room), [Music Player & Playlist Queue](problems/L2-design-patterns/33-music-player), [Stock Price Alert Service](problems/L2-design-patterns/34-stock-alerts), [Movie Ticket Booking](problems/L3-real-world-systems/35-movie-ticket-booking), [Expense Sharing](problems/L3-real-world-systems/36-splitwise), [Hotel Reservation System](problems/L3-real-world-systems/37-hotel-booking), [Food Delivery](problems/L3-real-world-systems/38-food-delivery), [Ride Hailing](problems/L3-real-world-systems/39-ride-hailing), [Chess Game](problems/L3-real-world-systems/40-chess), [Generic Cache with Pluggable Eviction](problems/L3-real-world-systems/41-cache-eviction), [Order Management System](problems/L3-real-world-systems/42-order-management), [Airline Reservation System](problems/L3-real-world-systems/43-airline-booking), [Calendar & Meeting Scheduler](problems/L3-real-world-systems/44-calendar-scheduler), [Q&A Platform](problems/L3-real-world-systems/46-stack-overflow), [Social Media Feed](problems/L3-real-world-systems/47-social-feed), [Online Auction System](problems/L3-real-world-systems/48-online-auction), [Online Coding Judge](problems/L3-real-world-systems/49-online-judge), [Hospital Appointment Management](problems/L3-real-world-systems/50-hospital-appointments), [Warehouse Inventory Management](problems/L3-real-world-systems/51-warehouse-inventory), [Customer Support Ticketing System](problems/L3-real-world-systems/52-support-ticketing), [Music Streaming Service](problems/L3-real-world-systems/53-music-streaming), [Bike / Scooter Sharing](problems/L3-real-world-systems/54-bike-sharing), [Restaurant POS & Kitchen Display](problems/L3-real-world-systems/55-restaurant-pos), [Parcel Locker System](problems/L3-real-world-systems/56-parcel-locker), [Rate Limiter Library](problems/L4-concurrency-infrastructure-components/57-rate-limiter), [Thread Pool & Task Scheduler](problems/L4-concurrency-infrastructure-components/58-thread-pool-scheduler), [In-Memory Pub/Sub Message Broker](problems/L4-concurrency-infrastructure-components/59-pub-sub-broker), [Key-Value Store with TTL & Transactions](problems/L4-concurrency-infrastructure-components/60-kv-store), [Distributed-Ready Job Scheduler](problems/L4-concurrency-infrastructure-components/63-cron-scheduler), [Load Balancer with Health Checks](problems/L4-concurrency-infrastructure-components/64-load-balancer), [Metrics Collector](problems/L4-concurrency-infrastructure-components/66-metrics-collector), [Unique ID Generator](problems/L4-concurrency-infrastructure-components/67-id-generator), [Multithreaded Web Crawler](problems/L4-concurrency-infrastructure-components/68-web-crawler), [In-Process Event Bus](problems/L4-concurrency-infrastructure-components/69-event-bus), [Micro-Batching Processor](problems/L4-concurrency-infrastructure-components/70-micro-batcher), [Service Registry & Discovery](problems/L4-concurrency-infrastructure-components/71-service-registry), [Resumable Download Manager](problems/L4-concurrency-infrastructure-components/73-download-manager), [Timer Service](problems/L4-concurrency-infrastructure-components/74-timing-wheel), [Digital Wallet & Ledger](problems/L5-staff-level-domains/75-digital-wallet), [Stock Exchange Order Matching Engine](problems/L5-staff-level-domains/76-order-matching-engine), [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine), [Feature Flag & Experimentation Service](problems/L5-staff-level-domains/79-feature-flags), [Search Autocomplete Engine](problems/L5-staff-level-domains/80-search-autocomplete), [Payment Gateway](problems/L5-staff-level-domains/81-payment-gateway), [Subscription Billing Engine](problems/L5-staff-level-domains/82-subscription-billing), [Loyalty & Rewards Points Engine](problems/L5-staff-level-domains/83-loyalty-points), [Flash Sale Inventory Reservation](problems/L5-staff-level-domains/84-flash-sale-inventory), [Pre-Trade Risk Management System](problems/L5-staff-level-domains/85-pretrade-risk), [Marketplace Settlement & Payouts](problems/L5-staff-level-domains/86-marketplace-settlement), [Product Catalogue Search & Facets](problems/L5-staff-level-domains/88-catalog-search), [Authorization Policy Engine](problems/L6-principal-level-frameworks/91-policy-engine), [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline), [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework), [Multi-Tenant Entitlements & Plans Platform](problems/L6-principal-level-frameworks/96-entitlements-platform), [Observability SDK](problems/L6-principal-level-frameworks/97-observability-sdk), [DAG Workflow Orchestrator](problems/L6-principal-level-frameworks/98-dag-orchestrator), [Schema Registry & Compatibility Checker](problems/L6-principal-level-frameworks/99-schema-registry), [Unit Testing Framework](problems/L6-principal-level-frameworks/100-test-framework) |
| **Observer** (61) | [Parking Lot](problems/L1-foundations/01-parking-lot), [Snake and Ladder](problems/L1-foundations/03-snake-and-ladder), [Library Management System](problems/L1-foundations/04-library-management), [Snake Game](problems/L1-foundations/07-snake-game), [Minesweeper](problems/L1-foundations/08-minesweeper), [Course Registration System](problems/L1-foundations/09-course-registration), [Restaurant Table Reservation](problems/L1-foundations/11-restaurant-reservation), [Task Manager with Reminders](problems/L1-foundations/14-todo-manager), [Gym Membership & Class Booking](problems/L1-foundations/15-gym-membership), [Logging Framework](problems/L2-design-patterns/19-logger-framework), [Elevator System](problems/L2-design-patterns/20-elevator-system), [Notification Service](problems/L2-design-patterns/21-notification-service), [Coffee Machine](problems/L2-design-patterns/22-coffee-machine), [Traffic Signal Controller](problems/L2-design-patterns/24-traffic-signal), [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home), [Group Chat Application](problems/L2-design-patterns/30-chat-room), [Layered Configuration Loader](problems/L2-design-patterns/32-config-loader), [Music Player & Playlist Queue](problems/L2-design-patterns/33-music-player), [Stock Price Alert Service](problems/L2-design-patterns/34-stock-alerts), [Movie Ticket Booking](problems/L3-real-world-systems/35-movie-ticket-booking), [Expense Sharing](problems/L3-real-world-systems/36-splitwise), [Food Delivery](problems/L3-real-world-systems/38-food-delivery), [Ride Hailing](problems/L3-real-world-systems/39-ride-hailing), [Generic Cache with Pluggable Eviction](problems/L3-real-world-systems/41-cache-eviction), [Order Management System](problems/L3-real-world-systems/42-order-management), [Calendar & Meeting Scheduler](problems/L3-real-world-systems/44-calendar-scheduler), [Project Board](problems/L3-real-world-systems/45-kanban-board), [Q&A Platform](problems/L3-real-world-systems/46-stack-overflow), [Social Media Feed](problems/L3-real-world-systems/47-social-feed), [Online Auction System](problems/L3-real-world-systems/48-online-auction), [Online Coding Judge](problems/L3-real-world-systems/49-online-judge), [Hospital Appointment Management](problems/L3-real-world-systems/50-hospital-appointments), [Warehouse Inventory Management](problems/L3-real-world-systems/51-warehouse-inventory), [Customer Support Ticketing System](problems/L3-real-world-systems/52-support-ticketing), [Music Streaming Service](problems/L3-real-world-systems/53-music-streaming), [Bike / Scooter Sharing](problems/L3-real-world-systems/54-bike-sharing), [Restaurant POS & Kitchen Display](problems/L3-real-world-systems/55-restaurant-pos), [Parcel Locker System](problems/L3-real-world-systems/56-parcel-locker), [In-Memory Pub/Sub Message Broker](problems/L4-concurrency-infrastructure-components/59-pub-sub-broker), [Circuit Breaker & Retry Library](problems/L4-concurrency-infrastructure-components/62-circuit-breaker), [Distributed-Ready Job Scheduler](problems/L4-concurrency-infrastructure-components/63-cron-scheduler), [Load Balancer with Health Checks](problems/L4-concurrency-infrastructure-components/64-load-balancer), [Metrics Collector](problems/L4-concurrency-infrastructure-components/66-metrics-collector), [In-Process Event Bus](problems/L4-concurrency-infrastructure-components/69-event-bus), [Service Registry & Discovery](problems/L4-concurrency-infrastructure-components/71-service-registry), [Resumable Download Manager](problems/L4-concurrency-infrastructure-components/73-download-manager), [Stock Exchange Order Matching Engine](problems/L5-staff-level-domains/76-order-matching-engine), [Configurable Workflow / State Machine Engine](problems/L5-staff-level-domains/78-workflow-engine), [Feature Flag & Experimentation Service](problems/L5-staff-level-domains/79-feature-flags), [Payment Gateway](problems/L5-staff-level-domains/81-payment-gateway), [Subscription Billing Engine](problems/L5-staff-level-domains/82-subscription-billing), [Loyalty & Rewards Points Engine](problems/L5-staff-level-domains/83-loyalty-points), [Pre-Trade Risk Management System](problems/L5-staff-level-domains/85-pretrade-risk), [Marketplace Settlement & Payouts](problems/L5-staff-level-domains/86-marketplace-settlement), [Collaborative Document Editor](problems/L5-staff-level-domains/87-collaborative-editor), [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator), [Event Sourcing & CQRS Framework](problems/L6-principal-level-frameworks/92-event-sourced-ledger), [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host), [Multi-Tenant Entitlements & Plans Platform](problems/L6-principal-level-frameworks/96-entitlements-platform), [DAG Workflow Orchestrator](problems/L6-principal-level-frameworks/98-dag-orchestrator), [Unit Testing Framework](problems/L6-principal-level-frameworks/100-test-framework) |
| **State** (33) | [Snake Game](problems/L1-foundations/07-snake-game), [Minesweeper](problems/L1-foundations/08-minesweeper), [Car Rental System](problems/L1-foundations/10-car-rental), [Restaurant Table Reservation](problems/L1-foundations/11-restaurant-reservation), [Card Game Engine](problems/L1-foundations/12-blackjack), [Gym Membership & Class Booking](problems/L1-foundations/15-gym-membership), [Vending Machine](problems/L2-design-patterns/17-vending-machine), [ATM System](problems/L2-design-patterns/18-atm), [Elevator System](problems/L2-design-patterns/20-elevator-system), [Traffic Signal Controller](problems/L2-design-patterns/24-traffic-signal), [Music Player & Playlist Queue](problems/L2-design-patterns/33-music-player), [Movie Ticket Booking](problems/L3-real-world-systems/35-movie-ticket-booking), [Hotel Reservation System](problems/L3-real-world-systems/37-hotel-booking), [Food Delivery](problems/L3-real-world-systems/38-food-delivery), [Ride Hailing](problems/L3-real-world-systems/39-ride-hailing), [Order Management System](problems/L3-real-world-systems/42-order-management), [Airline Reservation System](problems/L3-real-world-systems/43-airline-booking), [Project Board](problems/L3-real-world-systems/45-kanban-board), [Online Auction System](problems/L3-real-world-systems/48-online-auction), [Online Coding Judge](problems/L3-real-world-systems/49-online-judge), [Hospital Appointment Management](problems/L3-real-world-systems/50-hospital-appointments), [Customer Support Ticketing System](problems/L3-real-world-systems/52-support-ticketing), [Music Streaming Service](problems/L3-real-world-systems/53-music-streaming), [Bike / Scooter Sharing](problems/L3-real-world-systems/54-bike-sharing), [Parcel Locker System](problems/L3-real-world-systems/56-parcel-locker), [Circuit Breaker & Retry Library](problems/L4-concurrency-infrastructure-components/62-circuit-breaker), [Lock Manager with Leases](problems/L4-concurrency-infrastructure-components/72-distributed-lock), [Resumable Download Manager](problems/L4-concurrency-infrastructure-components/73-download-manager), [Configurable Workflow / State Machine Engine](problems/L5-staff-level-domains/78-workflow-engine), [Payment Gateway](problems/L5-staff-level-domains/81-payment-gateway), [Subscription Billing Engine](problems/L5-staff-level-domains/82-subscription-billing), [Flash Sale Inventory Reservation](problems/L5-staff-level-domains/84-flash-sale-inventory), [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator) |
| **Command** (24) | [Tic-Tac-Toe](problems/L1-foundations/02-tic-tac-toe), [ATM System](problems/L2-design-patterns/18-atm), [Elevator System](problems/L2-design-patterns/20-elevator-system), [Text Editor with Undo/Redo](problems/L2-design-patterns/25-text-editor), [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home), [Vector Shape Editor](problems/L2-design-patterns/31-shape-editor), [Chess Game](problems/L3-real-world-systems/40-chess), [Order Management System](problems/L3-real-world-systems/42-order-management), [Project Board](problems/L3-real-world-systems/45-kanban-board), [Online Coding Judge](problems/L3-real-world-systems/49-online-judge), [Warehouse Inventory Management](problems/L3-real-world-systems/51-warehouse-inventory), [Restaurant POS & Kitchen Display](problems/L3-real-world-systems/55-restaurant-pos), [Thread Pool & Task Scheduler](problems/L4-concurrency-infrastructure-components/58-thread-pool-scheduler), [Key-Value Store with TTL & Transactions](problems/L4-concurrency-infrastructure-components/60-kv-store), [Distributed-Ready Job Scheduler](problems/L4-concurrency-infrastructure-components/63-cron-scheduler), [Micro-Batching Processor](problems/L4-concurrency-infrastructure-components/70-micro-batcher), [Timer Service](problems/L4-concurrency-infrastructure-components/74-timing-wheel), [Digital Wallet & Ledger](problems/L5-staff-level-domains/75-digital-wallet), [Stock Exchange Order Matching Engine](problems/L5-staff-level-domains/76-order-matching-engine), [Configurable Workflow / State Machine Engine](problems/L5-staff-level-domains/78-workflow-engine), [Marketplace Settlement & Payouts](problems/L5-staff-level-domains/86-marketplace-settlement), [Collaborative Document Editor](problems/L5-staff-level-domains/87-collaborative-editor), [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator), [DAG Workflow Orchestrator](problems/L6-principal-level-frameworks/98-dag-orchestrator) |
| **Factory** (23) | [Parking Lot](problems/L1-foundations/01-parking-lot), [Tic-Tac-Toe](problems/L1-foundations/02-tic-tac-toe), [Shopping Cart & Checkout](problems/L1-foundations/05-shopping-cart), [Minesweeper](problems/L1-foundations/08-minesweeper), [Car Rental System](problems/L1-foundations/10-car-rental), [Card Game Engine](problems/L1-foundations/12-blackjack), [Online Exam / Quiz System](problems/L1-foundations/13-online-exam), [URL Shortener](problems/L1-foundations/16-url-shortener), [Notification Service](problems/L2-design-patterns/21-notification-service), [Multi-Gateway Payment Adapter](problems/L2-design-patterns/28-payment-adapter), [Group Chat Application](problems/L2-design-patterns/30-chat-room), [Stock Price Alert Service](problems/L2-design-patterns/34-stock-alerts), [Expense Sharing](problems/L3-real-world-systems/36-splitwise), [Ride Hailing](problems/L3-real-world-systems/39-ride-hailing), [Chess Game](problems/L3-real-world-systems/40-chess), [Social Media Feed](problems/L3-real-world-systems/47-social-feed), [Rate Limiter Library](problems/L4-concurrency-infrastructure-components/57-rate-limiter), [Thread Pool & Task Scheduler](problems/L4-concurrency-infrastructure-components/58-thread-pool-scheduler), [Database Connection Pool](problems/L4-concurrency-infrastructure-components/61-connection-pool), [Load Balancer with Health Checks](problems/L4-concurrency-infrastructure-components/64-load-balancer), [Stock Exchange Order Matching Engine](problems/L5-staff-level-domains/76-order-matching-engine), [Dependency Injection Container](problems/L6-principal-level-frameworks/89-di-container), [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline) |
| **Builder** (17) | [Snake and Ladder](problems/L1-foundations/03-snake-and-ladder), [Online Exam / Quiz System](problems/L1-foundations/13-online-exam), [Logging Framework](problems/L2-design-patterns/19-logger-framework), [Coffee Machine](problems/L2-design-patterns/22-coffee-machine), [Report Generator & Exporter](problems/L2-design-patterns/27-document-exporter), [Layered Configuration Loader](problems/L2-design-patterns/32-config-loader), [Hotel Reservation System](problems/L3-real-world-systems/37-hotel-booking), [Generic Cache with Pluggable Eviction](problems/L3-real-world-systems/41-cache-eviction), [Airline Reservation System](problems/L3-real-world-systems/43-airline-booking), [Circuit Breaker & Retry Library](problems/L4-concurrency-infrastructure-components/62-circuit-breaker), [Configurable Workflow / State Machine Engine](problems/L5-staff-level-domains/78-workflow-engine), [Search Autocomplete Engine](problems/L5-staff-level-domains/80-search-autocomplete), [Subscription Billing Engine](problems/L5-staff-level-domains/82-subscription-billing), [Product Catalogue Search & Facets](problems/L5-staff-level-domains/88-catalog-search), [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator), [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework), [DAG Workflow Orchestrator](problems/L6-principal-level-frameworks/98-dag-orchestrator) |
| **Chain of Responsibility** (15) | [Shopping Cart & Checkout](problems/L1-foundations/05-shopping-cart), [Course Registration System](problems/L1-foundations/09-course-registration), [ATM System](problems/L2-design-patterns/18-atm), [Logging Framework](problems/L2-design-patterns/19-logger-framework), [Notification Service](problems/L2-design-patterns/21-notification-service), [Layered Configuration Loader](problems/L2-design-patterns/32-config-loader), [Project Board](problems/L3-real-world-systems/45-kanban-board), [Q&A Platform](problems/L3-real-world-systems/46-stack-overflow), [Customer Support Ticketing System](problems/L3-real-world-systems/52-support-ticketing), [Multithreaded Web Crawler](problems/L4-concurrency-infrastructure-components/68-web-crawler), [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine), [Pre-Trade Risk Management System](problems/L5-staff-level-domains/85-pretrade-risk), [Authorization Policy Engine](problems/L6-principal-level-frameworks/91-policy-engine), [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline), [Observability SDK](problems/L6-principal-level-frameworks/97-observability-sdk) |
| **Composite** (15) | [Shopping Cart & Checkout](problems/L1-foundations/05-shopping-cart), [Employee Directory & Org Chart](problems/L1-foundations/06-employee-hierarchy), [Task Manager with Reminders](problems/L1-foundations/14-todo-manager), [In-Memory File System](problems/L2-design-patterns/23-file-system), [Calculator & Expression Evaluator](problems/L2-design-patterns/26-expression-evaluator), [Report Generator & Exporter](problems/L2-design-patterns/27-document-exporter), [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home), [Vector Shape Editor](problems/L2-design-patterns/31-shape-editor), [Calendar & Meeting Scheduler](problems/L3-real-world-systems/44-calendar-scheduler), [Restaurant POS & Kitchen Display](problems/L3-real-world-systems/55-restaurant-pos), [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine), [Dependency Injection Container](problems/L6-principal-level-frameworks/89-di-container), [Authorization Policy Engine](problems/L6-principal-level-frameworks/91-policy-engine), [Multi-Tenant Entitlements & Plans Platform](problems/L6-principal-level-frameworks/96-entitlements-platform), [Unit Testing Framework](problems/L6-principal-level-frameworks/100-test-framework) |
| **Proxy** (12) | [Music Streaming Service](problems/L3-real-world-systems/53-music-streaming), [Database Connection Pool](problems/L4-concurrency-infrastructure-components/61-connection-pool), [Service Registry & Discovery](problems/L4-concurrency-infrastructure-components/71-service-registry), [Lock Manager with Leases](problems/L4-concurrency-infrastructure-components/72-distributed-lock), [Feature Flag & Experimentation Service](problems/L5-staff-level-domains/79-feature-flags), [Flash Sale Inventory Reservation](problems/L5-staff-level-domains/84-flash-sale-inventory), [Dependency Injection Container](problems/L6-principal-level-frameworks/89-di-container), [Authorization Policy Engine](problems/L6-principal-level-frameworks/91-policy-engine), [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework), [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host), [Multi-Tenant Entitlements & Plans Platform](problems/L6-principal-level-frameworks/96-entitlements-platform), [Unit Testing Framework](problems/L6-principal-level-frameworks/100-test-framework) |
| **Decorator** (10) | [Car Rental System](problems/L1-foundations/10-car-rental), [Notification Service](problems/L2-design-patterns/21-notification-service), [Coffee Machine](problems/L2-design-patterns/22-coffee-machine), [Generic Cache with Pluggable Eviction](problems/L3-real-world-systems/41-cache-eviction), [Rate Limiter Library](problems/L4-concurrency-infrastructure-components/57-rate-limiter), [Key-Value Store with TTL & Transactions](problems/L4-concurrency-infrastructure-components/60-kv-store), [Circuit Breaker & Retry Library](problems/L4-concurrency-infrastructure-components/62-circuit-breaker), [Search Autocomplete Engine](problems/L5-staff-level-domains/80-search-autocomplete), [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline), [Observability SDK](problems/L6-principal-level-frameworks/97-observability-sdk) |
| **Repository** (8) | [Library Management System](problems/L1-foundations/04-library-management), [Course Registration System](problems/L1-foundations/09-course-registration), [URL Shortener](problems/L1-foundations/16-url-shortener), [Hotel Reservation System](problems/L3-real-world-systems/37-hotel-booking), [Warehouse Inventory Management](problems/L3-real-world-systems/51-warehouse-inventory), [Rate Limiter Library](problems/L4-concurrency-infrastructure-components/57-rate-limiter), [Digital Wallet & Ledger](problems/L5-staff-level-domains/75-digital-wallet), [Event Sourcing & CQRS Framework](problems/L6-principal-level-frameworks/92-event-sourced-ledger) |
| **Iterator** (7) | [Employee Directory & Org Chart](problems/L1-foundations/06-employee-hierarchy), [In-Memory File System](problems/L2-design-patterns/23-file-system), [Text Editor with Undo/Redo](problems/L2-design-patterns/25-text-editor), [Music Player & Playlist Queue](problems/L2-design-patterns/33-music-player), [Calendar & Meeting Scheduler](problems/L3-real-world-systems/44-calendar-scheduler), [Social Media Feed](problems/L3-real-world-systems/47-social-feed), [In-Memory Pub/Sub Message Broker](problems/L4-concurrency-infrastructure-components/59-pub-sub-broker) |
| **Specification** (7) | [Task Manager with Reminders](problems/L1-foundations/14-todo-manager), [In-Memory File System](problems/L2-design-patterns/23-file-system), [Airline Reservation System](problems/L3-real-world-systems/43-airline-booking), [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine), [Feature Flag & Experimentation Service](problems/L5-staff-level-domains/79-feature-flags), [Loyalty & Rewards Points Engine](problems/L5-staff-level-domains/83-loyalty-points), [Product Catalogue Search & Facets](problems/L5-staff-level-domains/88-catalog-search) |
| **Template method** (7) | [Online Exam / Quiz System](problems/L1-foundations/13-online-exam), [Report Generator & Exporter](problems/L2-design-patterns/27-document-exporter), [Thread Pool & Task Scheduler](problems/L4-concurrency-infrastructure-components/58-thread-pool-scheduler), [Digital Wallet & Ledger](problems/L5-staff-level-domains/75-digital-wallet), [Saga Orchestrator for Distributed Transactions](problems/L6-principal-level-frameworks/90-saga-orchestrator), [DAG Workflow Orchestrator](problems/L6-principal-level-frameworks/98-dag-orchestrator), [Unit Testing Framework](problems/L6-principal-level-frameworks/100-test-framework) |
| **Adapter** (6) | [Multi-Gateway Payment Adapter](problems/L2-design-patterns/28-payment-adapter), [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home), [Layered Configuration Loader](problems/L2-design-patterns/32-config-loader), [Payment Gateway](problems/L5-staff-level-domains/81-payment-gateway), [Event Sourcing & CQRS Framework](problems/L6-principal-level-frameworks/92-event-sourced-ledger), [API Gateway Middleware Pipeline](problems/L6-principal-level-frameworks/93-middleware-pipeline) |
| **Visitor** (6) | [Employee Directory & Org Chart](problems/L1-foundations/06-employee-hierarchy), [In-Memory File System](problems/L2-design-patterns/23-file-system), [Calculator & Expression Evaluator](problems/L2-design-patterns/26-expression-evaluator), [Report Generator & Exporter](problems/L2-design-patterns/27-document-exporter), [Vector Shape Editor](problems/L2-design-patterns/31-shape-editor), [Schema Registry & Compatibility Checker](problems/L6-principal-level-frameworks/99-schema-registry) |
| **Facade** (5) | [ATM System](problems/L2-design-patterns/18-atm), [Multi-Gateway Payment Adapter](problems/L2-design-patterns/28-payment-adapter), [Movie Ticket Booking](problems/L3-real-world-systems/35-movie-ticket-booking), [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host), [Observability SDK](problems/L6-principal-level-frameworks/97-observability-sdk) |
| **Mediator** (5) | [Smart Home Automation Hub](problems/L2-design-patterns/29-smart-home), [Group Chat Application](problems/L2-design-patterns/30-chat-room), [Food Delivery](problems/L3-real-world-systems/38-food-delivery), [In-Process Event Bus](problems/L4-concurrency-infrastructure-components/69-event-bus), [Collaborative Document Editor](problems/L5-staff-level-domains/87-collaborative-editor) |
| **Memento** (5) | [Text Editor with Undo/Redo](problems/L2-design-patterns/25-text-editor), [Chess Game](problems/L3-real-world-systems/40-chess), [Key-Value Store with TTL & Transactions](problems/L4-concurrency-infrastructure-components/60-kv-store), [Collaborative Document Editor](problems/L5-staff-level-domains/87-collaborative-editor), [Event Sourcing & CQRS Framework](problems/L6-principal-level-frameworks/92-event-sourced-ledger) |
| **Registry** (5) | [Calculator & Expression Evaluator](problems/L2-design-patterns/26-expression-evaluator), [Metrics Collector](problems/L4-concurrency-infrastructure-components/66-metrics-collector), [Dependency Injection Container](problems/L6-principal-level-frameworks/89-di-container), [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host), [Schema Registry & Compatibility Checker](problems/L6-principal-level-frameworks/99-schema-registry) |
| **Interpreter** (4) | [Calculator & Expression Evaluator](problems/L2-design-patterns/26-expression-evaluator), [Distributed-Ready Job Scheduler](problems/L4-concurrency-infrastructure-components/63-cron-scheduler), [Promotion & Pricing Rule Engine](problems/L5-staff-level-domains/77-pricing-rule-engine), [Authorization Policy Engine](problems/L6-principal-level-frameworks/91-policy-engine) |
| **Singleton** (4) | [Parking Lot](problems/L1-foundations/01-parking-lot), [Vending Machine](problems/L2-design-patterns/17-vending-machine), [Logging Framework](problems/L2-design-patterns/19-logger-framework), [Unique ID Generator](problems/L4-concurrency-infrastructure-components/67-id-generator) |
| **Producer–Consumer** (2) | [Bounded Blocking Queue](problems/L4-concurrency-infrastructure-components/65-blocking-queue), [Multithreaded Web Crawler](problems/L4-concurrency-infrastructure-components/68-web-crawler) |
| **Chain** (1) | [Product Catalogue Search & Facets](problems/L5-staff-level-domains/88-catalog-search) |
| **Disposable** (1) | [Extension Host for an IDE/CLI](problems/L6-principal-level-frameworks/95-plugin-host) |
| **Guarded Suspension** (1) | [Bounded Blocking Queue](problems/L4-concurrency-infrastructure-components/65-blocking-queue) |
| **Identity Map** (1) | [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework) |
| **Null Object** (1) | [Observability SDK](problems/L6-principal-level-frameworks/97-observability-sdk) |
| **Object Pool** (1) | [Database Connection Pool](problems/L4-concurrency-infrastructure-components/61-connection-pool) |
| **Prototype** (1) | [Vector Shape Editor](problems/L2-design-patterns/31-shape-editor) |
| **Unit of Work** (1) | [ORM / Data Mapper Framework](problems/L6-principal-level-frameworks/94-orm-framework) |

</details>

## Evaluation rubric

Every mock interview is scored out of 100. Full rubric: [`docs/evaluation-rubric.md`](docs/evaluation-rubric.md).

| Dimension | Weight |
|---|:-:|
| Requirements & clarification | 10 |
| Correctness | 20 |
| OOP fundamentals | 15 |
| SOLID | 15 |
| Design patterns | 10 |
| Extensibility | 10 |
| Maintainability & code quality | 10 |
| Concurrency & scalability | 10 |

| Level | Pass mark |
|---|:-:|
| L1–L2 | 65 |
| L3 | 70 |
| L4 | 75 |
| L5–L6 | 80 |

## Repository structure

```
.
├── README.md                 # this file
├── PROGRESS.md               # per-problem tracker with dates and scores
├── problems.json             # machine-readable catalogue of all problems
├── docs/
│   ├── how-to-practice.md    # interview script, questions to ask, red flags
│   ├── evaluation-rubric.md
│   ├── patterns-cheatsheet.md
│   └── solid-cheatsheet.md
├── templates/                # blank REQUIREMENTS and DESIGN templates
└── problems/
    └── L1-foundations/
        └── 01-parking-lot/
            ├── README.md         # status, learnings, how to run
            ├── REQUIREMENTS.md   # full specification
            ├── DESIGN.md         # class diagram, patterns, SOLID check, trade-offs
            ├── FEEDBACK.md       # mock interview scorecard
            ├── src/
            └── tests/
```

## Running the code

```bash
git clone https://github.com/<your-username>/lld-mastery.git
cd lld-mastery
npm install

# run every test
npm test

# run one problem's tests
npm test -- parking-lot
```

## Conventions

- **One problem, one folder.** Folder names are `NN-kebab-case`, numbered within the repo so they sort in roadmap order.
- **Design before code.** `DESIGN.md` is filled in before anything goes into `src/`.
- **Every pattern has a reason.** The design worksheet names the requirement that forced each pattern.
- **Money is never a float.** Amounts use integer minor units (paise) or a decimal type.
- **Time is injected.** No `new Date()` inside domain logic; a `Clock` is passed in so tests are deterministic.
- **Tests map to the spec.** At least one test per functional requirement (`FR1`, `FR2`…) and edge case (`EC1`…), named after the ID.
- **Commits** follow `feat(parking-lot): add pricing strategy`, `test(atm): cover dispense failure`, `docs(splitwise): add design`.

## References

- *Head First Design Patterns* — Freeman & Robson
- *Design Patterns: Elements of Reusable Object-Oriented Software* — Gamma, Helm, Johnson, Vlissides
- *Clean Architecture* — Robert C. Martin
- *Implementing Domain-Driven Design* — Vaughn Vernon
- *Java Concurrency in Practice* — Brian Goetz (concepts apply to any language)
- [Refactoring Guru — design patterns catalogue](https://refactoring.guru/design-patterns)

---

<div align="center">

Built as a long-term practice log. If it helps you, a ⭐ is appreciated.

</div>
