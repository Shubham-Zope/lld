package com.shubham.carrental.services;

import com.shubham.carrental.enums.AddOns;

public class InsurancePriceCalculator implements PriceCalculator {
  private final PriceCalculator calculator;

  public InsurancePriceCalculator(PriceCalculator calculator) {
    this.calculator = calculator;
  }

  @Override()
  public double calculate() {
    return calculator.calculate() + AddOns.INSURANCE.addOnPrice;
  }
}
