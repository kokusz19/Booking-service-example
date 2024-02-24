package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.model.dao.Service;
import com.kokusz19.udinfopark.model.dto.Company;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.Time;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelConverter {

    private final ServiceService serviceService;
    private final CompanyService companyService;

    public ModelConverter(ServiceService serviceService, CompanyService companyService) {
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
                reservation.getServices().stream().map(com.kokusz19.udinfopark.model.dao.Service::getServiceId).toList(),
                reservation.getReservorName(),
                reservation.getReservorPhoneNumber(),
                reservation.getReservorEmail());
    }

    public com.kokusz19.udinfopark.model.dao.Reservation convert(Reservation reservation) {
        List<Service> byIdList = serviceService.findByIds(reservation.getServiceIds());
        com.kokusz19.udinfopark.model.dao.Company company = convert(companyService.getOne(reservation.getCompanyId()));

        return new com.kokusz19.udinfopark.model.dao.Reservation(reservation.getReservationId(),
                company,
                byIdList,
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
        com.kokusz19.udinfopark.model.dao.Company company = convert(companyService.getOne(service.getCompanyId()));

        return new com.kokusz19.udinfopark.model.dao.Service(service.getServiceId(),
                service.getName(),
                company,
                service.getDescription(),
                service.getDurationMinutes());
    }




    public Time convert(com.kokusz19.udinfopark.model.dao.Time time) {
        return new Time(time.getHour(), time.getMinute());
    }

    public com.kokusz19.udinfopark.model.dao.Time convert(Time time) {
        return new com.kokusz19.udinfopark.model.dao.Time(time.getHour(), time.getMinute());
    }

}
