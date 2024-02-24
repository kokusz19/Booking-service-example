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
    Optional<Service> findByName(String name);

    @Query("SELECT service FROM Service service WHERE service.serviceId in :ids")
    List<Service> findByIds(
            @Param("ids") List<Integer> ids);
}
