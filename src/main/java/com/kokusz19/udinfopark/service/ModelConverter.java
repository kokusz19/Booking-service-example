package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.model.dao.Service;
import com.kokusz19.udinfopark.model.dto.Company;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.ServiceReservation;
import com.kokusz19.udinfopark.model.dto.Time;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelConverter {

    private final ServiceService serviceService;
    private final CompanyService companyService;

    public ModelConverter(@Lazy ServiceService serviceService, @Lazy CompanyService companyService) {
        this.serviceService = serviceService;
        this.companyService = companyService;
    }

    public Company convert(com.kokusz19.udinfopark.model.dao.Company company) {
        return new Company(company.getCompanyId(),
                company.getName(),
                company.getAddress(),
                convert(company.getOpenAt()),
                convert(company.getCloseAt()));
    }

    public com.kokusz19.udinfopark.model.dao.Company convert(Company company) {
        return new com.kokusz19.udinfopark.model.dao.Company(company.getCompanyId(),
                company.getName(),
                company.getAddress(),
                convert(company.getOpenAt()),
                convert(company.getCloseAt()));
    }

    public Reservation convert(com.kokusz19.udinfopark.model.dao.Reservation reservation) {
        return new Reservation(reservation.getReservationId(),
                reservation.getCompany().getCompanyId(),
                reservation.getServiceReservations().stream().map(this::convert).toList(),
                reservation.getReservorName(),
                reservation.getReservorPhoneNumber(),
                reservation.getReservorEmail());
    }

    public com.kokusz19.udinfopark.model.dao.Reservation convert(Reservation reservation) {
        List<Service> services = serviceService.findByIds(reservation.getServiceReservations().stream().map(ServiceReservation::getServiceId).toList());
        Preconditions.checkArgument(CollectionUtils.containsAll(reservation.getServiceReservations().stream().map(ServiceReservation::getServiceId).toList(), services.stream().map(Service::getServiceId).toList()), "There are missing service ids in the request");

        Company company = companyService.getOne(reservation.getCompanyId());
        Preconditions.checkNotNull(company, String.format("Company could not be found with [id=%s]", reservation.getCompanyId()));
        com.kokusz19.udinfopark.model.dao.Company companyDao = convert(company);

        List<com.kokusz19.udinfopark.model.dao.ServiceReservation> serviceReservations = reservation.getServiceReservations().stream().map(this::convert).toList();
        Preconditions.checkArgument(reservation.getServiceReservations().size() == serviceReservations.size(), "Some service reservations could not be converted!");

        return new com.kokusz19.udinfopark.model.dao.Reservation(reservation.getReservationId(),
                companyDao,
                serviceReservations,
                reservation.getReservorName(),
                reservation.getReservorPhoneNumber(),
                reservation.getReservorEmail());
    }

    public com.kokusz19.udinfopark.model.dto.Service convert(com.kokusz19.udinfopark.model.dao.Service service) {
        return new com.kokusz19.udinfopark.model.dto.Service(service.getServiceId(),
                service.getName(),
                service.getCompany().getCompanyId(),
                service.getDescription(),
                service.getDurationMinutes());
    }

    public com.kokusz19.udinfopark.model.dao.Service convert(com.kokusz19.udinfopark.model.dto.Service service) {
        Company companyDto = companyService.getOne(service.getCompanyId());
        Preconditions.checkNotNull(companyDto, String.format("Company not found with [id=%d]", service.getCompanyId()));

        com.kokusz19.udinfopark.model.dao.Company company = convert(companyDto);

        return new com.kokusz19.udinfopark.model.dao.Service(service.getServiceId(),
                service.getName(),
                company,
                service.getDescription(),
                service.getDurationMinutes());
    }

    public ServiceReservation convert(com.kokusz19.udinfopark.model.dao.ServiceReservation serviceReservation) {
        return new ServiceReservation(
                serviceReservation.getServiceReservationId(),
                serviceReservation.getService().getServiceId(),
                serviceReservation.getReservationStart()
        );
    }

    public com.kokusz19.udinfopark.model.dao.ServiceReservation convert(ServiceReservation serviceReservation) {
        com.kokusz19.udinfopark.model.dao.Service service = convert(serviceService.getOne(serviceReservation.getServiceId()));
        Preconditions.checkNotNull(service, String.format("Service could not be found [serviceId=%d]!", serviceReservation.getServiceId()));

        return new com.kokusz19.udinfopark.model.dao.ServiceReservation(
                serviceReservation.getServiceReservationId(),
                service,
                serviceReservation.getReservationStart()
        );
    }

    public Time convert(com.kokusz19.udinfopark.model.dao.Time time) {
        return new Time(time.getHour(), time.getMinute());
    }

    public com.kokusz19.udinfopark.model.dao.Time convert(Time time) {
        return new com.kokusz19.udinfopark.model.dao.Time(time.getHour(), time.getMinute());
    }

}
