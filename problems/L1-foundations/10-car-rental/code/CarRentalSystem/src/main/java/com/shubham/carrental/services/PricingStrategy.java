package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;

public interface PricingStrategy {
    double calculatePrice(Reservation reservation);
}
