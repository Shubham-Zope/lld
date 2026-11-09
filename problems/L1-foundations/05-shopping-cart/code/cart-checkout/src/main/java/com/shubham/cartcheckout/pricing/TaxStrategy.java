package com.shubham.cartcheckout.pricing;

import com.shubham.cartcheckout.model.Money;

public interface TaxStrategy {
  Money calculate(Money taxableAmount);
}
