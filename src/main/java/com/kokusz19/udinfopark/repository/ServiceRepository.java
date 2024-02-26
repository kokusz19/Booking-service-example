package com.kokusz19.udinfopark.repository;

import com.kokusz19.udinfopark.model.dao.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    @Query("SELECT service FROM Service service WHERE service.company.companyId = :companyId AND service.name = :serviceName")
    Optional<Service> findByCompanyIdAndServiceName(
            @Param("companyId") int companyId,
            @Param("serviceName") String serviceName);

    @Query("SELECT service FROM Service service WHERE service.serviceId in :ids")
    List<Service> findByIds(
            @Param("ids") List<Integer> ids);

    @Query("SELECT service FROM Service service WHERE service.company.companyId = :companyId")
    List<Service> findByCompanyId(
            @Param("companyId") int companyId);
}
