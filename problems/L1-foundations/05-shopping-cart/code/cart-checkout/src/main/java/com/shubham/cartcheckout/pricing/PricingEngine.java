package com.shubham.cartcheckout.pricing;

import com.shubham.cartcheckout.coupon.Coupon;
import com.shubham.cartcheckout.model.*;
import com.shubham.cartcheckout.repository.InMemoryCatalog;
import java.time.LocalDate;
import java.util.*;

/**
 * Deterministic pricing pipeline: current prices -> coupon discounts -> GST -> shipping -> total.
 */
public class PricingEngine {
  private final InMemoryCatalog catalog;
  private final TaxRegistry taxRegistry;
  private final Money shippingFee;

  public PricingEngine(InMemoryCatalog catalog, TaxRegistry taxRegistry, Money shippingFee) {
    this.catalog = catalog;
    this.taxRegistry = taxRegistry;
    this.shippingFee = shippingFee;
  }

  public PriceBreakdown calculate(Cart cart, List<Coupon> coupons) {
    List<WorkLine> work = new ArrayList<>();
    Money subtotal = Money.ZERO;
    int totalQuantity = 0;
    for (CartItem item : cart.items()) {
      Product product =
          catalog
              .find(item.productId())
              .orElseThrow(
                  () -> new IllegalArgumentException("Product not found: " + item.productId()));
      ProductVariant variant =
          Optional.ofNullable(product.variants().get(item.variantId()))
              .orElseThrow(
                  () -> new IllegalArgumentException("Variant not found: " + item.variantId()));
      Money lineSubtotal = variant.price().multiply(item.quantity());
      subtotal = subtotal.add(lineSubtotal);
      totalQuantity += item.quantity();
      work.add(new WorkLine(product, variant, item, lineSubtotal));
    }

    Money remainingDiscountable = subtotal;
    Money itemDiscounts = Money.ZERO;
    Money shippingDiscount = Money.ZERO;
    List<String> applied = new ArrayList<>();
    boolean freeShipping = false;
    for (Coupon coupon : coupons) {
      if (coupon.isValidOn(LocalDate.now())) continue;
      if (freeShipping && coupon.isFreeShipping()) continue;
      Money requested =
          coupon
              .discount(remainingDiscountable, totalQuantity)
              .max(Money.ZERO)
              .min(remainingDiscountable);
      if (!requested.isZero()) {
        allocateDiscount(work, requested, coupon);
        itemDiscounts = itemDiscounts.add(requested);
        remainingDiscountable = remainingDiscountable.subtract(requested);
      }
      if (coupon.isFreeShipping()) freeShipping = true;
      applied.add(coupon.code());
      if (!coupon.stackable()) break;
    }

    List<PricedLine> priced = new ArrayList<>();
    Money totalTax = Money.ZERO;
    for (WorkLine line : work) {
      Money lineDiscount =
          line.discounts.stream().map(DiscountLine::amount).reduce(Money.ZERO, Money::add);
      Money taxable = line.subtotal.subtract(lineDiscount).max(Money.ZERO);
      Money tax = taxRegistry.forCategory(line.product.category()).calculate(taxable);
      totalTax = totalTax.add(tax);
      priced.add(
          new PricedLine(
              line.product.id(),
              line.product.name(),
              line.variant.id(),
              line.variant.label(),
              line.item.quantity(),
              line.variant.price(),
              line.subtotal,
              line.discounts,
              taxable,
              tax,
              taxable.add(tax)));
    }
    Money shipping = freeShipping ? Money.ZERO : shippingFee;
    if (freeShipping) shippingDiscount = shippingFee;
    Money grandTotal =
        subtotal
            .subtract(itemDiscounts)
            .max(Money.ZERO)
            .add(totalTax)
            .add(shipping)
            .max(Money.ZERO);
    return new PriceBreakdown(
        priced, subtotal, itemDiscounts, shipping, shippingDiscount, totalTax, grandTotal, applied);
  }

  private void allocateDiscount(List<WorkLine> lines, Money discount, Coupon coupon) {
    Money remaining = discount;
    Money remainingSubtotal =
        lines.stream()
            .map(l -> l.subtotal.subtract(l.discountTotal()).max(Money.ZERO))
            .reduce(Money.ZERO, Money::add);
    for (int i = 0; i < lines.size(); i++) {
      WorkLine line = lines.get(i);
      Money eligible = line.subtotal.subtract(line.discountTotal()).max(Money.ZERO);
      if (eligible.isZero() || remaining.isZero()) continue;
      Money share =
          (i == lines.size() - 1)
              ? remaining
              : new Money(
                  discount
                      .amount()
                      .multiply(eligible.amount())
                      .divide(remainingSubtotal.amount(), 2, java.math.RoundingMode.HALF_UP));
      share = share.min(eligible).min(remaining);
      if (!share.isZero())
        line.discounts.add(new DiscountLine(coupon.code(), coupon.description(), share));
      remaining = remaining.subtract(share);

      remainingSubtotal = remainingSubtotal.subtract(eligible);
    }
  }

  private static class WorkLine {
    final Product product;
    final ProductVariant variant;
    final CartItem item;
    final Money subtotal;
    final List<DiscountLine> discounts = new ArrayList<>();

    WorkLine(Product p, ProductVariant v, CartItem i, Money s) {
      product = p;
      variant = v;
      item = i;
      subtotal = s;
    }

    Money discountTotal() {
      return discounts.stream().map(DiscountLine::amount).reduce(Money.ZERO, Money::add);
    }
  }
}
