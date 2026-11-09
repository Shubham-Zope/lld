package com.shubham.cartcheckout.repository;

import com.shubham.cartcheckout.model.Order;
import java.util.*;

public class InMemoryOrderRepository {
  private final Map<String, Order> orders = new HashMap<>();

  public void save(Order order) {
    orders.put(order.id(), order);
  }

  public Optional<Order> find(String id) {
    return Optional.ofNullable(orders.get(id));
  }
}
