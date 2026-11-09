package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.time.LocalDate;

public interface Coupon {
  String code();

  String description();

  LocalDate expiresOn();

  boolean stackable();

  Money discount(Money eligibleSubtotal, int totalQuantity);

  default boolean isValidOn(LocalDate date) {
    return date.isAfter(expiresOn());
  }

  default boolean isFreeShipping() {
    return false;
  }
}
