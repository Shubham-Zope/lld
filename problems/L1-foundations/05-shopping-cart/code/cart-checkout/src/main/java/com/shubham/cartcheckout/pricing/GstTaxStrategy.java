package com.shubham.cartcheckout.pricing;

import com.shubham.cartcheckout.model.Money;
import java.math.BigDecimal;

public record GstTaxStrategy(BigDecimal ratePercent) implements TaxStrategy {
  public Money calculate(Money taxableAmount) {
    return taxableAmount.multiply(ratePercent).multiply(new BigDecimal("0.01"));
  }
}
