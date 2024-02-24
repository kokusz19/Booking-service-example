package com.kokusz19.udinfopark.repository;

import com.kokusz19.udinfopark.model.dao.Reservation;
import com.kokusz19.udinfopark.model.dao.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByreservorNameAndServices(String reservorName, List<Service> services);
}
