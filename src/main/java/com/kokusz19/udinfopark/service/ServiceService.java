package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.api.ServiceApi;
import com.kokusz19.udinfopark.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceService implements ServiceApi {

    private final ServiceRepository serviceRepository;
    private final ServiceReservationService serviceReservationService;
    private final ModelConverter modelConverter;

    public ServiceService(ServiceRepository serviceRepository, ServiceReservationService serviceReservationService, ModelConverter modelConverter) {
        this.serviceRepository = serviceRepository;
        this.serviceReservationService = serviceReservationService;
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

    public List<com.kokusz19.udinfopark.model.dto.Service> getByCompanyId(int companyId) {
        return serviceRepository.findByCompanyId(companyId).stream().map(modelConverter::convert).toList();
    }

    @Override
    public int create(com.kokusz19.udinfopark.model.dto.Service subject) {
        Optional<com.kokusz19.udinfopark.model.dao.Service> byName = serviceRepository.findByCompanyIdAndServiceName(subject.getCompanyId(), subject.getName());
        Preconditions.checkArgument(byName.isEmpty(), "Service already exists under the company!");
        return serviceRepository.save(modelConverter.convert(subject)).getServiceId();
    }

    @Override
    public com.kokusz19.udinfopark.model.dto.Service update(int id, com.kokusz19.udinfopark.model.dto.Service subject) {
        subject.setServiceId(id);
        return modelConverter.convert(serviceRepository.save(modelConverter.convert(subject)));
    }

    @Override
    public boolean delete(int id) {
        Preconditions.checkArgument(serviceReservationService.getByServiceId(id).isEmpty(), "Can't delete service, as it has reservations registered to it!");

        return serviceRepository.findById(id).map(company -> {
            serviceRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
