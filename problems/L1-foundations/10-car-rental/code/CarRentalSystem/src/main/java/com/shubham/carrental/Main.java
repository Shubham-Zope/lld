package com.shubham.carrental;

import com.shubham.carrental.entities.BookingSlot;
import com.shubham.carrental.entities.Branch;
import com.shubham.carrental.entities.Customer;
import com.shubham.carrental.entities.Reservation;
import com.shubham.carrental.entities.Vehicle;
import com.shubham.carrental.enums.AddOns;
import com.shubham.carrental.enums.VehicleType;
import com.shubham.carrental.services.PricingService;
import com.shubham.carrental.services.StandardPricingStrategy;
import com.shubham.carrental.services.WeekendPricingStrategy;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final PricingService STANDARD = new PricingService(new StandardPricingStrategy());
    private static final PricingService WEEKEND = new PricingService(new WeekendPricingStrategy());
    private static int reservationCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        // ---------- Setup ----------
        Vehicle car1 = new Vehicle("V1", "MH12-AB-1111", VehicleType.CAR);
        Vehicle car2 = new Vehicle("V2", "MH12-AB-2222", VehicleType.CAR);
        Vehicle car3 = new Vehicle("V3", "MH12-AB-3333", VehicleType.CAR);
        Vehicle bike1 = new Vehicle("V4", "MH12-CD-4444", VehicleType.BIKE);

        Branch pune = new Branch("B1", "Pune Central", List.of(car1, car2), List.of());
        pune.addVehicle(car3);
        pune.addVehicle(bike1);

        Customer alice = new Customer("C1", "Alice");
        Customer bob = new Customer("C2", "Bob");
        Customer charlie = new Customer("C3", "Charlie");
        pune.addCustomer(alice);
        pune.addCustomer(bob);
        pune.addCustomer(charlie);

        BookingSlot dec1to4 = slot(2026, 12, 1, 2026, 12, 4);   // Tue -> Fri, 3 days

        // ---------- 1. Search availability ----------
        section("1. Search CARs at " + pune.getBranchName() + " for Dec 1 -> Dec 4");
        printVehicles(pune.searchAvailableVehicles(VehicleType.CAR, dec1to4));

        // ---------- 2. Reserve ----------
        section("2. Alice reserves a CAR Dec 1 -> Dec 4 with GPS");
        Reservation aliceRes = reserve(pune, alice, VehicleType.CAR, dec1to4, STANDARD, AddOns.GPS);
        System.out.println("Price: " + aliceRes.calculatePrice() + "   (300 x 3 days + GPS 100)");

        // ---------- 3. No double reservation ----------
        section("3. Bob tries Alice's car for overlapping dates Dec 2 -> Dec 5");
        BookingSlot dec2to5 = slot(2026, 12, 2, 2026, 12, 5);
        Reservation clash = new Reservation(nextId(), bob, pune, car1, dec2to5, STANDARD);
        System.out.println("Reserve " + car1.getVehiclePlate() + " -> " + clash.reserveVehicle() + " (overlap rejected)");

        System.out.println("Bob reserves any free CAR instead:");
        Reservation bobRes = reserve(pune, bob, VehicleType.CAR, dec2to5, STANDARD);

        section("4. Back-to-back booking on Alice's car Dec 4 -> Dec 6 (allowed)");
        BookingSlot dec4to6 = slot(2026, 12, 4, 2026, 12, 6);
        System.out.println("Free CARs for Dec 4 -> Dec 6:");
        printVehicles(pune.searchAvailableVehicles(VehicleType.CAR, dec4to6));

        // ---------- 5. Concurrency ----------
        section("5. 10 threads race for " + car3.getVehiclePlate() + " on Dec 10 -> Dec 12");
        BookingSlot dec10to12 = slot(2026, 12, 10, 2026, 12, 12);
        AtomicInteger winners = new AtomicInteger();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reservation attempt = new Reservation(nextId(), charlie, pune, car3, dec10to12, STANDARD);
            threads.add(Thread.ofPlatform().start(() -> {
                if (attempt.reserveVehicle()) {
                    winners.incrementAndGet();
                }
            }));
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("Successful reservations: " + winners.get() + " (expected 1)");

        // ---------- 6. Modify ----------
        section("6. Alice extends her booking to Dec 1 -> Dec 5");
        BookingSlot dec1to5 = slot(2026, 12, 1, 2026, 12, 5);
        System.out.println("Modify -> " + aliceRes.modifyBookingSlot(dec1to5));
        alice.setReservation(aliceRes.getVehicle(), aliceRes.getBookingSlot());
        System.out.println("Days: " + aliceRes.getBookingDays() + ", price: " + aliceRes.calculatePrice()
                + "   (300 x 4 days + GPS 100)");

        assert bobRes != null;
        System.out.println("Bob moves to Dec 1 -> Dec 3 on " + bobRes.getVehicle().getVehiclePlate() + ":");
        System.out.println("Modify -> " + bobRes.modifyBookingSlot(slot(2026, 12, 1, 2026, 12, 3))
                + ", Bob now has " + bobRes.getBookingDays() + " days");
        bob.setReservation(bobRes.getVehicle(), bobRes.getBookingSlot());

        // ---------- 7. Cancel ----------
        section("7. Bob cancels");
        bobRes.cancelReservation();
        bob.setReservation(null, null);
        System.out.println("Status: " + bobRes.getStatus());
        System.out.println("Free CARs for Dec 2 -> Dec 5 now:");
        printVehicles(pune.searchAvailableVehicles(VehicleType.CAR, dec2to5));

        // ---------- 8. Pick-up ----------
        section("8. Alice picks up " + aliceRes.getVehicle().getVehiclePlate());
        System.out.println("Pick-up -> " + aliceRes.bookVehicle());
        alice.setBooking(aliceRes.getVehicle(), aliceRes.getBookingSlot());
        alice.setReservation(null, null);
        System.out.println("Reservation: " + aliceRes.getStatus() + ", vehicle: " + aliceRes.getVehicle().getVehicleStatus());

        // ---------- 9. Return: 2 days late + damaged ----------
        section("9. Alice returns 2 days late with damage");
        aliceRes.setExtraDaysAdded(2);
        aliceRes.vehicleIsDamaged();            // before pricing: damage fine reads the status
        double finalPrice = aliceRes.calculatePrice();
        aliceRes.getVehicle().cancelBooking(aliceRes.getBookingSlot());
        alice.setBooking(null, null);
        System.out.println("Final price: " + finalPrice);
        System.out.println("  = 300 x (4 + 2) days + 150 x 2 late days + 2500 damage + GPS 100");
        System.out.println("Vehicle status: " + aliceRes.getVehicle().getVehicleStatus());
        System.out.println("Free CARs for Dec 20 -> Dec 22 (damaged car excluded):");
        printVehicles(pune.searchAvailableVehicles(VehicleType.CAR, slot(2026, 12, 20, 2026, 12, 22)));

        // ---------- 10. Weekend pricing ----------
        section("10. Charlie rents a BIKE for the weekend Dec 5 -> Dec 7 with insurance");
        Reservation charlieRes = reserve(pune, charlie, VehicleType.BIKE, slot(2026, 12, 5, 2026, 12, 7), WEEKEND, AddOns.INSURANCE);
        assert charlieRes != null;
        System.out.println("Price: " + charlieRes.calculatePrice() + "   (150 x 2 days x 1.5 + insurance 250)");
    }

    // Finds a free vehicle at the branch and reserves it; returns null if none is free.
    private static Reservation reserve(Branch branch, Customer customer, VehicleType type, BookingSlot slot,
                                       PricingService pricingService, AddOns... addOns) {
        for (Vehicle vehicle : branch.searchAvailableVehicles(type, slot)) {
            Reservation reservation = new Reservation(nextId(), customer, branch, vehicle, slot, pricingService);
            if (reservation.reserveVehicle()) {
                for (AddOns addOn : addOns) {
                    reservation.addAddOns(addOn);
                }
                customer.setReservation(vehicle, slot);
                System.out.println(customer.getCustomerName() + " -> " + reservation.getReservationId()
                        + " on " + vehicle.getVehiclePlate() + " [" + reservation.getStatus() + "]");
                return reservation;
            }
        }
        System.out.println(customer.getCustomerName() + " -> no " + type + " available");
        return null;
    }

    private static BookingSlot slot(int y1, int m1, int d1, int y2, int m2, int d2) {
        return new BookingSlot(toDate(LocalDate.of(y1, m1, d1)), toDate(LocalDate.of(y2, m2, d2)));
    }

    private static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static String nextId() {
        return "R" + (++reservationCounter);
    }

    private static void printVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("  (none)");
        }
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getId() + " " + v.getVehiclePlate() + " " + v.getType() + " " + v.getVehicleStatus());
        }
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}
