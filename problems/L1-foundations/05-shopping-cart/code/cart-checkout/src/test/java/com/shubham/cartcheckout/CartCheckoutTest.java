package com.shubham.cartcheckout;

import com.shubham.cartcheckout.coupon.CouponFactory;
import com.shubham.cartcheckout.model.*;
import com.shubham.cartcheckout.pricing.*;
import com.shubham.cartcheckout.repository.*;
import com.shubham.cartcheckout.service.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CartCheckoutTest {
    @Test void checkoutCreatesSnapshotConsumesStockAndClearsCart() {
        InMemoryCatalog catalog = new InMemoryCatalog();
        catalog.save(new Product("P1", "Book", "BOOKS", Map.of("STD", new ProductVariant("STD", "Standard", Money.of("100")))));
        InventoryService inventory = new InventoryService(); inventory.setStock("P1", "STD", 3);
        CouponFactory coupons = CouponFactory.demoCoupons();
        PricingEngine pricing = new PricingEngine(catalog, TaxRegistry.demoRegistry(), Money.of("50"));
        Cart cart = new Cart("C1", "U1"); cart.add(new CartItem("P1", "STD", 2));
        InMemoryOrderRepository orders = new InMemoryOrderRepository();
        CheckoutService checkout = new CheckoutService(inventory, coupons, pricing, orders);

        CheckoutResult result = checkout.checkout(cart);

        assertTrue(result.successful());
        assertEquals(Money.of("250"), result.order().snapshot().grandTotal());
        assertTrue(cart.items().isEmpty());
        assertEquals(1, inventory.available("P1", "STD"));
        assertTrue(orders.find(result.order().id()).isPresent());
    }

    @Test void checkoutReturnsStockProblemWithoutCreatingOrder() {
        InMemoryCatalog catalog = new InMemoryCatalog();
        catalog.save(new Product("P1", "Book", "BOOKS", Map.of("STD", new ProductVariant("STD", "Standard", Money.of("100")))));
        InventoryService inventory = new InventoryService(); inventory.setStock("P1", "STD", 1);
        CouponFactory coupons = CouponFactory.demoCoupons();
        PricingEngine pricing = new PricingEngine(catalog, TaxRegistry.demoRegistry(), Money.of("50"));
        Cart cart = new Cart("C1", "U1"); cart.add(new CartItem("P1", "STD", 2));
        CheckoutService checkout = new CheckoutService(inventory, coupons, pricing, new InMemoryOrderRepository());

        CheckoutResult result = checkout.checkout(cart);

        assertFalse(result.successful());
        assertFalse(result.problems().isEmpty());
        assertEquals(1, inventory.available("P1", "STD"));
    }

    @Test void moneyUsesTwoDecimalPlaces() {
        assertEquals(Money.of("10.01"), Money.of("10.005"));
    }
}
