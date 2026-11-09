package com.shubham.cartcheckout.repository;

import com.shubham.cartcheckout.model.Product;
import java.util.*;

public class InMemoryCatalog {
  private final Map<String, Product> products = new HashMap<>();

  public void save(Product product) {
    products.put(product.id(), product);
  }

  public Optional<Product> find(String id) {
    return Optional.ofNullable(products.get(id));
  }
}
