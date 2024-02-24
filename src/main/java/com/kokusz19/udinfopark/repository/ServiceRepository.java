package com.kokusz19.udinfopark.repository;

import com.kokusz19.udinfopark.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Optional<Service> findByName(String name);
}
