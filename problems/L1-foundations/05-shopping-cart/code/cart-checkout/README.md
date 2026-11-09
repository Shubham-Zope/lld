# Shopping Cart & Checkout (Java 17 + Gradle)

An interview-oriented, runnable in-memory LLD implementation.

## Run in IntelliJ IDEA
1. Extract/open this `cart-checkout` folder in IntelliJ.
2. Select **Open as Project** and trust the project.
3. Set **Project SDK** to JDK 17 or newer (the Gradle toolchain requests Java 17).
4. Allow IntelliJ to import/sync the Gradle project.
5. Open `src/main/java/com/shubham/cartcheckout/demo/Main.java`.
6. Click the green run icon beside `main`, or run `gradle run` from the Gradle tool window.

If you have JDK 25 installed, it can run this Java 17-compatible project; make sure Gradle is configured to use that installed JDK. The source language level is Java 17.

## Implemented features
- Add/update/remove product-variant cart lines; 50 distinct-line limit.
- Stock check when adding/updating and again at checkout.
- Two-coupon maximum; expiry and stackability handling.
- Percentage, flat, buy-X-get-Y and free-shipping coupon strategies.
- GST strategy selected by product category.
- Decimal INR calculations using `BigDecimal`; no floating-point money.
- Line-level discount reasons, taxable amount, GST and total.
- Guest-to-user cart merge with duplicate-line combination and stock cap.
- Checkout re-prices from the current catalog, reserves stock, snapshots prices in an order and clears the cart.

## Design map
- **Strategy:** `Coupon` implementations and `TaxStrategy` implementations.
- **Factory / registry:** `CouponFactory` registers and resolves coupon strategies.
- **Pricing pipeline:** `PricingEngine` applies coupon strategies in deterministic cart coupon order, then tax and shipping.
- **Value object:** immutable `Money` wraps `BigDecimal` and rounds to two decimal places using HALF_UP.
- **Snapshot:** `Order` stores an immutable `PriceBreakdown` built at checkout.

## Important production considerations
This is an end-to-end domain implementation with in-memory repositories, not a production payment system. For production:
- Persist carts, coupons, orders and inventory in a database.
- Use a single transaction and conditional stock updates / inventory reservations to prevent overselling under concurrency.
- Revalidate coupon eligibility, usage limits, customer restrictions and promotion budgets inside checkout transaction.
- Add idempotency keys for checkout and payment callbacks.
- Define jurisdiction-specific GST treatment, inclusive/exclusive tax, HSN rules and invoice rounding with finance/legal requirements.
- The included BxGy example is deliberately scoped to same-price units; a general promotion engine should model eligible SKUs, cheapest-item selection and per-line allocation explicitly.
- If coupon stacking is disallowed, current behavior stops processing subsequent coupons after a non-stackable coupon. Coupon order is the order applied to the cart.

## Extend
To add a coupon, implement `Coupon` and register it in `CouponFactory`. To add a tax category/rate, register another `TaxStrategy` in `TaxRegistry`. Existing pricing code does not need a new `if/else` branch for each discount type.
