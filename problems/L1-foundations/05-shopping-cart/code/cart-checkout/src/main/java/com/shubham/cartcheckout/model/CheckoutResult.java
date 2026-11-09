package com.shubham.cartcheckout.model;

import java.util.List;

public record CheckoutResult(Order order, List<String> problems) {
  public CheckoutResult {
    problems = List.copyOf(problems);
  }

  public static CheckoutResult success(Order order) {
    return new CheckoutResult(order, List.of());
  }

  public static CheckoutResult failure(List<String> problems) {
    return new CheckoutResult(null, problems);
  }

  public boolean successful() {
    return order != null;
  }
}
