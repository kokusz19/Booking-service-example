package com.kokusz19.udinfopark.repository;

import com.kokusz19.udinfopark.model.dao.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
