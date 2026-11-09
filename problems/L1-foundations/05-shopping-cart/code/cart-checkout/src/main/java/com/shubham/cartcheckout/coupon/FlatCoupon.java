package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.time.LocalDate;

public record FlatCoupon(
    String code, String description, LocalDate expiresOn, boolean stackable, Money value)
    implements Coupon {
  public Money discount(Money subtotal, int quantity) {
    return value.min(subtotal);
  }
}
