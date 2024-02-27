package com.kokusz19.udinfopark.repository;

import com.kokusz19.udinfopark.model.dao.ServiceReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceReservationRepository extends JpaRepository<ServiceReservation, Integer> {
    @Query("SELECT sr FROM ServiceReservation sr WHERE sr.service.company.companyId = :companyId AND sr.reservationStart BETWEEN :fromDate AND :toDate")
    Optional<ServiceReservation> findByCompanyIdAndDate(
            @Param("companyId") int serviceId,
            @Param("fromDate") Date fromTime,
            @Param("toDate") Date toDate);

    @Query("SELECT sr FROM ServiceReservation sr WHERE sr.service.serviceId = :serviceId")
    List<ServiceReservation> findByServiceId(
            @Param("serviceId") int serviceId
    );
}
