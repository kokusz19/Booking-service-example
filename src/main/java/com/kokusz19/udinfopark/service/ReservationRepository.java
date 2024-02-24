package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.model.Reservation;
import com.kokusz19.udinfopark.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByreservorNameAndServices(String reservorName, List<Service> services);
}
