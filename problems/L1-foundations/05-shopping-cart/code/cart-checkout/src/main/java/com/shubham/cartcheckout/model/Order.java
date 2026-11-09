package com.shubham.cartcheckout.model;

import java.time.Instant;
import java.util.UUID;

/** Order contains a checkout-time snapshot and is independent of later catalog changes. */
public record Order(String id, String customerId, PriceBreakdown snapshot, Instant createdAt) {

  public static Order from(String customerId, PriceBreakdown snapshot) {
    return new Order(UUID.randomUUID().toString(), customerId, snapshot, Instant.now());
  }
}
