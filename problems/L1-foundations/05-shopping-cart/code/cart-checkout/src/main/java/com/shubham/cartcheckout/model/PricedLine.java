package com.shubham.cartcheckout.model;

import java.util.List;

public record PricedLine(
    String productId,
    String productName,
    String variantId,
    String variantLabel,
    int quantity,
    Money unitPrice,
    Money lineSubtotal,
    List<DiscountLine> discounts,
    Money taxableAmount,
    Money tax,
    Money total) {
  public PricedLine {
    discounts = List.copyOf(discounts);
  }
}
