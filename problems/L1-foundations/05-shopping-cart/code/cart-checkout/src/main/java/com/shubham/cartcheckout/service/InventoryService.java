package com.shubham.cartcheckout.service;

import java.util.HashMap;
import java.util.Map;

/** In production, reserve stock atomically in the database during checkout. */
public class InventoryService {
  private final Map<String, Integer> stock = new HashMap<>();

  private String key(String productId, String variantId) {
    return productId + "::" + variantId;
  }

  public void setStock(String productId, String variantId, int quantity) {
    stock.put(key(productId, variantId), quantity);
  }

  public int available(String productId, String variantId) {
    return stock.getOrDefault(key(productId, variantId), 0);
  }

  public boolean hasStock(String productId, String variantId, int quantity) {
    return available(productId, variantId) >= quantity;
  }

  public boolean reserve(String productId, String variantId, int quantity) {
    if (!hasStock(productId, variantId, quantity)) return false;
    stock.put(key(productId, variantId), available(productId, variantId) - quantity);
    return true;
  }
}
