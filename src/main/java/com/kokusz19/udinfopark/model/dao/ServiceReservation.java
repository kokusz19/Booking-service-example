package com.kokusz19.udinfopark.model.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int serviceReservationId;
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    @Column(name = "reservation_start")
    private Date reservationStart;

}
