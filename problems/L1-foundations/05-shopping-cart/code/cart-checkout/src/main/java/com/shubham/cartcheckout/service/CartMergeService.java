package com.shubham.cartcheckout.service;

import com.shubham.cartcheckout.model.*;
import java.util.ArrayList;
import java.util.List;

public class CartMergeService {
    private final InventoryService inventory;
    public CartMergeService(InventoryService inventory) { this.inventory = inventory; }
    /** Merge guest lines into user cart. Duplicate product+variant keys are combined, capped by stock. */
    public List<String> merge(Cart guest, Cart user) {
        List<String> problems = new ArrayList<>();
        for (CartItem item : guest.items()) {
            int existing = user.items().stream().filter(i -> i.key().equals(item.key())).mapToInt(CartItem::quantity).sum();
            int desired = existing + item.quantity();
            int accepted = Math.min(desired, inventory.available(item.productId(), item.variantId()));
            if (accepted < desired) problems.add("Quantity for " + item.key() + " reduced to available stock (" + accepted + ")");
            if (accepted > existing) user.add(new CartItem(item.productId(), item.variantId(), accepted - existing));
        }
        for (String code : guest.couponCodes()) {
            try { user.addCoupon(code); } catch (RuntimeException ex) { problems.add("Coupon " + code + " not merged: " + ex.getMessage()); }
        }
        guest.clear();
        return problems;
    }
}
