package com.shubham.cartcheckout.pricing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/** GST rates are configurable by product category. */
public class TaxRegistry {
  private final Map<String, TaxStrategy> strategies = new HashMap<>();

  public void register(String category, TaxStrategy strategy) {
    strategies.put(category, strategy);
  }

  public TaxStrategy forCategory(String category) {
    return strategies.getOrDefault(category, new GstTaxStrategy(new BigDecimal("0")));
  }

  public static TaxRegistry demoRegistry() {
    TaxRegistry registry = new TaxRegistry();

    registry.register("BOOKS", new GstTaxStrategy(new BigDecimal("0")));
    registry.register("APPAREL", new GstTaxStrategy(new BigDecimal("5")));
    registry.register("ELECTRONICS", new GstTaxStrategy(new BigDecimal("18")));
    return registry;
  }
}
