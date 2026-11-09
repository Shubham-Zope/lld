package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.time.LocalDate;

public record FreeShippingCoupon(
    String code, String description, LocalDate expiresOn, boolean stackable) implements Coupon {
  public Money discount(Money subtotal, int quantity) {
    return Money.ZERO;
  }

  @Override
  public boolean isFreeShipping() {
    return true;
  }
}
