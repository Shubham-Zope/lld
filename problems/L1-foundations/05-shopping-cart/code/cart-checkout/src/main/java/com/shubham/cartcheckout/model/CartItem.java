package com.shubham.cartcheckout.model;

public record CartItem(String productId, String variantId, int quantity) {
  public CartItem {
    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
  }

  public CartItem withQuantity(int newQuantity) {
    return new CartItem(productId, variantId, newQuantity);
  }

  public String key() {
    return productId + "::" + variantId;
  }
}
