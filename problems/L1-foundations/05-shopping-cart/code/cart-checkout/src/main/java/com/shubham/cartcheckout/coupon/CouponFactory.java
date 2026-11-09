package com.shubham.cartcheckout.coupon;

import com.shubham.cartcheckout.model.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/** Central coupon registry; add new coupon definitions here without changing pricing logic. */
public class CouponFactory {
  private final Map<String, Coupon> coupons = new HashMap<>();

  public void register(Coupon coupon) {
    coupons.put(coupon.code().toUpperCase(Locale.ROOT), coupon);
  }

  public Optional<Coupon> find(String code) {
    return Optional.ofNullable(coupons.get(code.toUpperCase(Locale.ROOT)));
  }

  public static CouponFactory demoCoupons() {

    CouponFactory factory = new CouponFactory();
    LocalDate expiry = LocalDate.now().plusDays(30);
    factory.register(
        new PercentageCoupon(
            "SAVE10", "10% off eligible items", expiry, true, new BigDecimal("10")));
    factory.register(new FlatCoupon("FLAT100", "₹100 off", expiry, false, Money.of("100")));
    factory.register(new FreeShippingCoupon("SHIPFREE", "Free shipping", expiry, true));
    return factory;
  }
}
