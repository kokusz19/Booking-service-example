package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.ServiceReservationApi;
import com.kokusz19.udinfopark.model.dto.ServiceReservation;
import com.kokusz19.udinfopark.repository.ServiceReservationRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ServiceReservationService implements ServiceReservationApi {

    private final ServiceReservationRepository serviceReservationRepository;
    private final ModelConverter modelConverter;

    public ServiceReservationService(ServiceReservationRepository serviceReservationRepository, ModelConverter modelConverter) {
        this.serviceReservationRepository = serviceReservationRepository;
        this.modelConverter = modelConverter;
    }

    Optional<com.kokusz19.udinfopark.model.dto.ServiceReservation> findByServiceIdAndDate(com.kokusz19.udinfopark.model.dto.Service service, ServiceReservation serviceReservation) {
        return serviceReservationRepository.findByCompanyIdAndDate(service.getCompanyId(), DateUtils.addMinutes(serviceReservation.getReservationStart(), -service.getDurationMinutes()), DateUtils.addMinutes(serviceReservation.getReservationStart(), service.getDurationMinutes()))
                .map(modelConverter::convert);
    }

    @Override
    public List<ServiceReservation> getAll() {
        return serviceReservationRepository.findAll().stream().map(modelConverter::convert).toList();
    }

    public List<ServiceReservation> getByServiceId(int serviceId) {
        return serviceReservationRepository.findByServiceId(serviceId).stream().map(modelConverter::convert).toList();
    }

    @Override
    public ServiceReservation getOne(int id) {
        return serviceReservationRepository.findById(id).map(modelConverter::convert).orElse(null);
    }
    @Override
    public int create(ServiceReservation subject) {
        //return serviceReservationRepository.save(modelConverter.convert(subject)).getServiceReservationId();
        return 0;
    }

    public com.kokusz19.udinfopark.model.dao.ServiceReservation save(com.kokusz19.udinfopark.model.dao.ServiceReservation serviceReservation) {
        return serviceReservationRepository.save(serviceReservation);
    }

    @Override
    public ServiceReservation update(int id, ServiceReservation subject) {
        return modelConverter.convert(serviceReservationRepository.save(modelConverter.convert(subject)));
    }

    @Override
    public boolean delete(int id) {
        return serviceReservationRepository.findById(id).map(company -> {
            serviceReservationRepository.delete(company);
            return true;
        }).orElse(false);
    }
}
