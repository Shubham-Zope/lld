package com.shubham.cartcheckout.demo;

import com.shubham.cartcheckout.coupon.CouponFactory;
import com.shubham.cartcheckout.model.*;
import com.shubham.cartcheckout.pricing.*;
import com.shubham.cartcheckout.repository.*;
import com.shubham.cartcheckout.service.*;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    InMemoryCatalog catalog = new InMemoryCatalog();
    catalog.save(
        new Product(
            "P100",
            "Wireless Mouse",
            "ELECTRONICS",
            Map.of(
                "BLACK", new ProductVariant("BLACK", "Black", Money.of("1499.00")),
                "WHITE", new ProductVariant("WHITE", "White", Money.of("1599.00")))));

    InventoryService inventory = new InventoryService();
    inventory.setStock("P100", "BLACK", 10);
    inventory.setStock("P100", "WHITE", 5);
    InMemoryCartRepository cartRepo = new InMemoryCartRepository();
    InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();
    CouponFactory couponFactory = CouponFactory.demoCoupons();
    PricingEngine pricing = new PricingEngine(catalog, TaxRegistry.demoRegistry(), Money.of("80"));
    CartService cartService = new CartService(catalog, cartRepo, inventory, couponFactory, pricing);
    CheckoutService checkout = new CheckoutService(inventory, couponFactory, pricing, orderRepo);

    Cart cart = cartService.createCart("CART-1", "CUSTOMER-42");
    cartService.addItem(cart.id(), "P100", "BLACK", 2);
    cartService.applyCoupon(cart.id(), "SAVE10");
    cartService.applyCoupon(cart.id(), "SHIPFREE");
    printSummary(cartService.summary(cart.id()));

    CheckoutResult result = checkout.checkout(cart);
    if (result.successful()) {
      System.out.println("\nORDER CREATED: " + result.order().id());
      System.out.println("Order total snapshot: " + result.order().snapshot().grandTotal());
      System.out.println("Cart cleared: " + cart.items().isEmpty());
    } else System.out.println("Checkout problems: " + result.problems());
  }

  private static void printSummary(PriceBreakdown p) {
    System.out.println("=== CART SUMMARY ===");
    for (PricedLine line : p.lines()) {
      System.out.printf(
          "%s (%s) x%d | unit %s | subtotal %s%n",
          line.productName(),
          line.variantLabel(),
          line.quantity(),
          line.unitPrice(),
          line.lineSubtotal());
      for (DiscountLine d : line.discounts())
        System.out.println("  Discount " + d.code() + ": -" + d.amount() + " (" + d.reason() + ")");
      System.out.println(
          "  Taxable: "
              + line.taxableAmount()
              + " | GST: "
              + line.tax()
              + " | Line total: "
              + line.total());
    }
    System.out.println("Subtotal: " + p.subtotal());
    System.out.println("Item discounts: -" + p.itemDiscounts());
    System.out.println(
        "Shipping: " + p.shipping() + " (shipping discount: " + p.shippingDiscount() + ")");
    System.out.println("Tax: " + p.tax());
    System.out.println("GRAND TOTAL: " + p.grandTotal());
    System.out.println("Coupons: " + p.appliedCoupons());
  }
}
