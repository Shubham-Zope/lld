package com.shubham.cartcheckout.repository;

import com.shubham.cartcheckout.model.Cart;
import java.util.*;

public class InMemoryCartRepository {
  private final Map<String, Cart> carts = new HashMap<>();

  public void save(Cart cart) {
    carts.put(cart.id(), cart);
  }

  public Optional<Cart> find(String id) {
    return Optional.ofNullable(carts.get(id));
  }
}
