package com.shubham.cartcheckout.model;

import java.util.List;

public record PriceBreakdown(
    List<PricedLine> lines,
    Money subtotal,
    Money itemDiscounts,
    Money shipping,
    Money shippingDiscount,
    Money tax,
    Money grandTotal,
    List<String> appliedCoupons) {

  public PriceBreakdown {
    lines = List.copyOf(lines);
    appliedCoupons = List.copyOf(appliedCoupons);
  }
}
