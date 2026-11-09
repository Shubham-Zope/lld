package com.shubham.cartcheckout.model;

import java.util.Map;

public record Product(
    String id, String name, String category, Map<String, ProductVariant> variants) {
  public Product {
    variants = Map.copyOf(variants);
  }
}
