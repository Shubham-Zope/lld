package com.shubham.cartcheckout.service;

import com.shubham.cartcheckout.coupon.Coupon;
import com.shubham.cartcheckout.coupon.CouponFactory;
import com.shubham.cartcheckout.model.*;
import com.shubham.cartcheckout.pricing.PricingEngine;
import com.shubham.cartcheckout.repository.InMemoryOrderRepository;
import java.time.LocalDate;
import java.util.*;

public class CheckoutService {
  private final InventoryService inventory;
  private final CouponFactory couponFactory;
  private final PricingEngine pricing;
  private final InMemoryOrderRepository orders;

  public CheckoutService(
      InventoryService inventory,
      CouponFactory couponFactory,
      PricingEngine pricing,
      InMemoryOrderRepository orders) {
    this.inventory = inventory;
    this.couponFactory = couponFactory;
    this.pricing = pricing;
    this.orders = orders;
  }

  public CheckoutResult checkout(Cart cart) {
    List<String> problems = new ArrayList<>();
    if (cart.items().isEmpty()) return CheckoutResult.failure(List.of("Cart is empty"));
    for (CartItem item : cart.items()) {

      if (!inventory.hasStock(item.productId(), item.variantId(), item.quantity()))
        problems.add(
            "Insufficient stock for "
                + item.productId()
                + "/"
                + item.variantId()
                + ". Available: "
                + inventory.available(item.productId(), item.variantId()));
    }
    List<Coupon> validCoupons = new ArrayList<>();
    for (String code : cart.couponCodes()) {
      Optional<Coupon> found = couponFactory.find(code);
      if (found.isEmpty()) problems.add("Coupon " + code + " is no longer valid");
      else if (found.get().isValidOn(LocalDate.now()))
        problems.add("Coupon " + code + " has expired");
      else validCoupons.add(found.get());
    }
    if (!problems.isEmpty()) return CheckoutResult.failure(problems);
    PriceBreakdown snapshot;
    try {
      snapshot = pricing.calculate(cart, validCoupons);
    } catch (RuntimeException ex) {
      return CheckoutResult.failure(List.of(ex.getMessage()));
    }
    // Reserve all stock. In production this block must be one DB transaction / atomic reservation.
    List<CartItem> reserved = new ArrayList<>();
    for (CartItem item : cart.items()) {
      if (!inventory.reserve(item.productId(), item.variantId(), item.quantity())) {
        problems.add(
            "Stock changed during checkout for " + item.productId() + "/" + item.variantId());
        // Roll back reservations in this in-memory example.
        for (CartItem previous : reserved)
          inventory.setStock(
              previous.productId(),
              previous.variantId(),
              inventory.available(previous.productId(), previous.variantId())
                  + previous.quantity());
        return CheckoutResult.failure(problems);
      }
      reserved.add(item);
    }
    Order order = Order.from(cart.ownerId(), snapshot);
    orders.save(order);
    cart.clear();
    return CheckoutResult.success(order);
  }
}
