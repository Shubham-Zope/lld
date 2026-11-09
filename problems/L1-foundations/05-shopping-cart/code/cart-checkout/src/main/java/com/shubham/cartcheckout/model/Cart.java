package com.shubham.cartcheckout.model;

import java.util.*;

public class Cart {
  public static final int MAX_ITEMS = 50;
  private final String id;
  private final String ownerId;
  private final LinkedHashMap<String, CartItem> items = new LinkedHashMap<>();
  private final List<String> couponCodes = new ArrayList<>();

  public Cart(String id, String ownerId) {
    this.id = id;
    this.ownerId = ownerId;
  }

  public String id() {
    return id;
  }

  public String ownerId() {
    return ownerId;
  }

  public Collection<CartItem> items() {
    return Collections.unmodifiableCollection(items.values());
  }

  public List<String> couponCodes() {
    return List.copyOf(couponCodes);
  }

  public void add(CartItem item) {
    CartItem existing = items.get(item.key());
    if (existing == null && items.size() >= MAX_ITEMS)
      throw new IllegalStateException("Cart cannot contain more than 50 line items");

    items.put(
        item.key(),
        existing == null ? item : existing.withQuantity(existing.quantity() + item.quantity()));
  }

  public void update(String productId, String variantId, int quantity) {
    String key = productId + "::" + variantId;
    if (!items.containsKey(key)) throw new IllegalArgumentException("Item not found in cart");
    if (quantity <= 0) items.remove(key);
    else items.put(key, new CartItem(productId, variantId, quantity));
  }

  public void remove(String productId, String variantId) {
    items.remove(productId + "::" + variantId);
  }

  public void addCoupon(String code) {
    if (couponCodes.contains(code)) return;
    if (couponCodes.size() >= 2) throw new IllegalStateException("Maximum 2 coupons are allowed");
    couponCodes.add(code);
  }

  public void clear() {
    items.clear();
    couponCodes.clear();
  }
}
