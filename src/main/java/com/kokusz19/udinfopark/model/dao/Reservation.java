package com.kokusz19.udinfopark.model.dao;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int reservationId;
    @NotNull
    @Valid
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany
    @JoinTable(
            name = "reservation_service_reservation",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_reservation_id")
    )
    private List<ServiceReservation> serviceReservations;

    @NotEmpty
    @Column(name = "reservor_name")
    private String reservorName;
    @NotEmpty
    @Column(name = "reservor_phone_number")
    private String reservorPhoneNumber;
    @NotEmpty
    @Column(name = "reservor_email")
    private String reservorEmail;
}
