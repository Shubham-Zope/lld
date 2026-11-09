package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PercentageCoupon(
    String code, String description, LocalDate expiresOn, boolean stackable, BigDecimal percentage)
    implements Coupon {
  public Money discount(Money subtotal, int quantity) {
    return subtotal.multiply(percentage).multiply(new BigDecimal("0.01")).min(subtotal);
  }
}
