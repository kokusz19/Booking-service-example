package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.CompanyApi;
import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.api.ServiceApi;
import com.kokusz19.udinfopark.model.dto.Company;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.Service;
import com.kokusz19.udinfopark.model.dto.Time;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
                15
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
                "Register an account in the libraryy",
                20
        ));

        reservationApi.create(new Reservation(
                0,
                hospitalId,
                List.of(hospitalEmergencyServices),
                "Sir Edmund Blackadder",
                "+1-800-273-8255",
                "blackadder@gmail.com"
        ));

        reservationApi.create(new Reservation(
                0,
                libraryId,
                List.of(libraryRegistration, libraryBookPickup),
                "Santa Claus",
                "951-262-3062",
                "santa@claus.com"
        ));

        reservationApi.create(new Reservation(
                0,
                libraryId,
                List.of(libraryBookPickup, libraryBookDropOff),
                "Baldrick",
                "626-792-8247",
                "baldrick@yahoo.com"
        ));
    }
}
