package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.time.LocalDate;

/** Simplified same-SKU promotion: for every X paid units, Y cheapest units are free. */
public record BuyXGetYCoupon(
    String code,
    String description,
    LocalDate expiresOn,
    boolean stackable,
    int buy,
    int get,
    Money unitPrice)
    implements Coupon {
  public Money discount(Money subtotal, int totalQuantity) {
    if (buy <= 0 || get <= 0) throw new IllegalArgumentException("Buy/get must be positive");
    int freeUnits = (totalQuantity / (buy + get)) * get;

    return unitPrice.multiply(freeUnits).min(subtotal);
  }
}
