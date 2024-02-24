package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.ServiceApi;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceService implements ServiceApi {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.kokusz19.udinfopark.model.Service> getAll() {
        return serviceRepository.findAll();
    }

    @Override
    public com.kokusz19.udinfopark.model.Service getOne(int id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public int create(com.kokusz19.udinfopark.model.Service subject) {
        Optional<com.kokusz19.udinfopark.model.Service> byName = serviceRepository.findByName(subject.getName());
        if(byName.isPresent()) {
            throw new RuntimeException("Service already exists!");
        }
        return serviceRepository.save(subject).getServiceId();
    }

    @Override
    public com.kokusz19.udinfopark.model.Service update(int id, com.kokusz19.udinfopark.model.Service subject) {
        subject.setServiceId(id);
        return serviceRepository.save(subject);
    }

    @Override
    public boolean delete(int id) {
        return serviceRepository.findById(id).map(company -> {
            serviceRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
