package com.shubham.cartcheckout.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Immutable INR money value. Never use floating point for monetary values. */
public record Money(BigDecimal amount) implements Comparable<Money> {
  public static final String CURRENCY = "INR";
  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money {
    Objects.requireNonNull(amount, "amount");
    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public static Money of(String amount) {
    return new Money(new BigDecimal(amount));
  }

  public static Money of(long amount) {
    return new Money(BigDecimal.valueOf(amount));
  }

  public Money add(Money other) {
    return new Money(amount.add(other.amount));
  }

  public Money subtract(Money other) {
    return new Money(amount.subtract(other.amount));
  }

  public Money multiply(int quantity) {
    return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
  }

  public Money multiply(BigDecimal factor) {
    return new Money(amount.multiply(factor));
  }

  public Money min(Money other) {
    return compareTo(other) <= 0 ? this : other;
  }

  public Money max(Money other) {
    return compareTo(other) >= 0 ? this : other;
  }

  public boolean isNegative() {
    return amount.signum() < 0;
  }

  public boolean isZero() {
    return amount.signum() == 0;
  }

  @Override
  public int compareTo(Money other) {
    return amount.compareTo(other.amount);
  }

  @Override
  public String toString() {
    return "₹" + amount.toPlainString();
  }
}
