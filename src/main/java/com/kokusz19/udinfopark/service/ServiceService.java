package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.ServiceApi;
import com.kokusz19.udinfopark.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceService implements ServiceApi {

    private final ServiceRepository serviceRepository;
    private final ModelConverter modelConverter;

    public ServiceService(ServiceRepository serviceRepository, ModelConverter modelConverter) {
        this.serviceRepository = serviceRepository;
        this.modelConverter = modelConverter;
    }

    public List<com.kokusz19.udinfopark.model.dao.Service> findByIds(List<Integer> ids) {
        return serviceRepository.findByIds(ids);
    }

    @Override
    public List<com.kokusz19.udinfopark.model.dto.Service> getAll() {
        return serviceRepository.findAll().stream().map(modelConverter::convert).toList();
    }

    @Override
    public com.kokusz19.udinfopark.model.dto.Service getOne(int id) {
        return serviceRepository.findById(id).map(modelConverter::convert).orElse(null);
    }

    @Override
    public int create(com.kokusz19.udinfopark.model.dto.Service subject) {
        Optional<com.kokusz19.udinfopark.model.dao.Service> byName = serviceRepository.findByName(subject.getName());
        if(byName.isPresent()) {
            throw new RuntimeException("Service already exists!");
        }
        return serviceRepository.save(modelConverter.convert(subject)).getServiceId();
    }

    @Override
    public com.kokusz19.udinfopark.model.dto.Service update(int id, com.kokusz19.udinfopark.model.dto.Service subject) {
        subject.setServiceId(id);
        return modelConverter.convert(serviceRepository.save(modelConverter.convert(subject)));
    }

    @Override
    public boolean delete(int id) {
        return serviceRepository.findById(id).map(company -> {
            serviceRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
