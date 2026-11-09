package com.shubham.cartcheckout.service;

import com.shubham.cartcheckout.coupon.Coupon;
import com.shubham.cartcheckout.coupon.CouponFactory;
import com.shubham.cartcheckout.model.*;
import com.shubham.cartcheckout.pricing.PricingEngine;
import com.shubham.cartcheckout.repository.*;
import java.util.*;

public class CartService {
    private final InMemoryCatalog catalog; private final InMemoryCartRepository carts;
    private final InventoryService inventory; private final CouponFactory coupons; private final PricingEngine pricing;
    public CartService(InMemoryCatalog catalog, InMemoryCartRepository carts, InventoryService inventory, CouponFactory coupons, PricingEngine pricing) {
        this.catalog=catalog; this.carts=carts; this.inventory=inventory; this.coupons=coupons; this.pricing=pricing;
    }
    public Cart createCart(String cartId, String ownerId) { Cart cart = new Cart(cartId, ownerId); carts.save(cart); return cart; }
    public void addItem(String cartId, String productId, String variantId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = catalog.find(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (!product.variants().containsKey(variantId)) throw new IllegalArgumentException("Variant not found");
        int requested = quantity + cart.items().stream().filter(i -> i.key().equals(productId + "::" + variantId)).mapToInt(CartItem::quantity).sum();
        if (!inventory.hasStock(productId, variantId, requested)) throw new IllegalArgumentException("Insufficient stock. Available: " + inventory.available(productId, variantId));
        cart.add(new CartItem(productId, variantId, quantity));
    }
    public void updateQuantity(String cartId, String productId, String variantId, int quantity) {
        Cart cart = getCart(cartId);
        if (quantity > 0 && !inventory.hasStock(productId, variantId, quantity)) throw new IllegalArgumentException("Insufficient stock");
        cart.update(productId, variantId, quantity);
    }
    public void removeItem(String cartId, String productId, String variantId) { getCart(cartId).remove(productId, variantId); }
    public void applyCoupon(String cartId, String code) {
        Coupon coupon = coupons.find(code).orElseThrow(() -> new IllegalArgumentException("Unknown coupon: " + code));
        if (coupon.isValidOn(java.time.LocalDate.now())) throw new IllegalArgumentException("Coupon expired");
        getCart(cartId).addCoupon(coupon.code());
    }
    public PriceBreakdown summary(String cartId) {
        Cart cart = getCart(cartId); List<Coupon> valid = new ArrayList<>();
        for (String code : cart.couponCodes()) coupons.find(code).ifPresent(valid::add);
        return pricing.calculate(cart, valid);
    }
    public Cart getCart(String id) { return carts.find(id).orElseThrow(() -> new IllegalArgumentException("Cart not found: " + id)); }
}
