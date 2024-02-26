package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.CompanyApi;
import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.api.ServiceApi;
import com.kokusz19.udinfopark.model.dto.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final CompanyApi companyApi;
    private final ServiceApi serviceApi;
    private final ReservationApi reservationApi;

    public DatabaseInitializer(CompanyApi companyApi, ServiceService serviceApi, ReservationService reservationApi) {
        this.companyApi = companyApi;
        this.serviceApi = serviceApi;
        this.reservationApi = reservationApi;
    }

    @Override
    public void run(String... args) throws Exception {

        int hospitalId = companyApi.create(new Company(
                0,
                "Hospital",
                "4032 Debrecen",
                new Time(0, 0),
                new Time(23, 59)
        ));
        int libraryId = companyApi.create(new Company(
                0,
                "Library",
                "4031 Debrecen",
                new Time(8, 0),
                new Time(20, 0)
        ));
        int mcDonaldsId = companyApi.create(new Company(
                0,
                "McDonald's",
                "4036 Debrecen",
                new Time(0, 0),
                new Time(23, 59)
        ));

        int hospitalEmergencyServices = serviceApi.create(new Service(
                0,
                "Emergency services",
                hospitalId,
                "Emergency services",
                30
        ));
        int hospitalBasicServices = serviceApi.create(new Service(
                0,
                "Basic services",
                hospitalId,
                "Basic services",
                25
        ));

        int libraryBookPickup = serviceApi.create(new Service(
                0,
                "Book pickup",
                libraryId,
                "Pickup spot reservation for picking up reserved books",
                15
        ));
        int libraryBookDropOff = serviceApi.create(new Service(
                0,
                "Book drop off",
                libraryId,
                "Drop off spot reservation for dropping off expired books",
                10
        ));
        int libraryRegistration = serviceApi.create(new Service(
                0,
                "Registration",
                libraryId,
                "Register an account in the library",
                20
        ));
        int mcDonaldsOrder = serviceApi.create(new Service(
                0,
                "Order",
                mcDonaldsId,
                "Make an order",
                30
        ));

        reservationApi.create(new Reservation(
                0,
                hospitalId,
                List.of(new ServiceReservation(hospitalEmergencyServices, new Date(124, Calendar.FEBRUARY, 24, 0, 0))),
                "Sir Edmund Blackadder",
                "+1-800-273-8255",
                "blackadder@gmail.com"
        ));
        reservationApi.create(new Reservation(
                0,
                libraryId,
                List.of(new ServiceReservation(libraryRegistration, new Date(124, Calendar.FEBRUARY, 24, 16, 30)), new ServiceReservation(libraryBookPickup, new Date(124, Calendar.FEBRUARY, 24, 17, 30))),
                "Santa Claus",
                "951-262-3062",
                "santa@claus.com"
        ));
        reservationApi.create(new Reservation(
                0,
                libraryId,
                List.of(new ServiceReservation(libraryBookPickup, new Date(124, Calendar.FEBRUARY, 24, 18, 0)), new ServiceReservation(libraryBookDropOff, new Date(124, Calendar.FEBRUARY, 24, 18, 30))),
                "Baldrick",
                "626-792-8247",
                "baldrick@yahoo.com"
        ));
        reservationApi.create(new Reservation(
                0,
                mcDonaldsId,
                List.of(new ServiceReservation(mcDonaldsOrder, new Date(124, Calendar.FEBRUARY, 24, 12, 0))),
                "Test User",
                "123-456-7890",
                "testUser@test.com"
        ));
    }
}
