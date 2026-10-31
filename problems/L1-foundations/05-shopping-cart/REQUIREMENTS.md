# 05 · Shopping Cart & Checkout — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 45 min
> **Patterns usually relevant:** Strategy, Chain of Responsibility, Composite (bundled products), Factory

## Problem statement

Design the cart and checkout module of an e-commerce store. Customers add products with variants to a cart, apply coupons, see a price breakdown with taxes and shipping, and check out into an order. Discounts and tax rules change frequently and must be pluggable.

## Functional requirements

- [ ] **FR1** Add, update quantity, remove line items (product + variant)
- [ ] **FR2** Validate stock at add time and again at checkout
- [ ] **FR3** Apply coupons: percentage, flat, buy-X-get-Y, free shipping; define stacking rules
- [ ] **FR4** Compute subtotal, discounts, tax (GST by category), shipping and grand total
- [ ] **FR5** Persist cart for guests and merge into user cart on login
- [ ] **FR6** Checkout creates an Order with a snapshot of prices; cart is cleared

## Non-functional requirements

- [ ] **NFR1** Money must use fixed-point/decimal, never floats
- [ ] **NFR2** Price computation deterministic and explainable (line-level breakdown)
- [ ] **NFR3** New discount type added without touching existing ones

## Constraints & assumptions

- Max 50 line items per cart
- Max 2 coupons per cart
- Single currency (INR)

## Edge cases to handle

- [ ] **EC1** Price changes between add-to-cart and checkout
- [ ] **EC2** Coupon expired at checkout
- [ ] **EC3** Coupon makes total negative
- [ ] **EC4** Item goes out of stock during checkout
- [ ] **EC5** Guest cart merge with duplicate items

## Expected behaviour

- cart.summary() returns line items, each discount applied with reason, taxes and totals
- checkout() returns Order or a list of validation problems the UI can display

## Design concepts this problem exercises

- Value objects (Money)
- Strategy / Chain for pricing rules
- Snapshotting
- Rounding rules

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Which discounts? | Percentage, flat, BOGO, free shipping. More will come. |
| Can coupons stack? | At most two, and only one percentage coupon. |
| Taxes? | GST rate depends on product category. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Multi-currency
- Flash sale with limited quantity
- Seller-funded vs platform-funded discounts split

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.
