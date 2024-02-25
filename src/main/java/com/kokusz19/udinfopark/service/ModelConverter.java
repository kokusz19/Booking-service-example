package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.model.dao.Service;
import com.kokusz19.udinfopark.model.dto.Company;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.Time;
import org.apache.commons.collections4.ListUtils;
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
                reservation.getServices().stream().map(com.kokusz19.udinfopark.model.dao.Service::getServiceId).toList(),
                reservation.getReservorName(),
                reservation.getReservorPhoneNumber(),
                reservation.getReservorEmail());
    }

    public com.kokusz19.udinfopark.model.dao.Reservation convert(Reservation reservation) {
        List<Service> byIdList = serviceService.findByIds(reservation.getServiceIds());
        List<Integer> missingServiceIds = ListUtils.subtract(reservation.getServiceIds(), byIdList.stream().map(Service::getServiceId).toList());
        Preconditions.checkArgument(missingServiceIds.isEmpty(), String.format("There are missing service ids in the request [ids=%s]", String.join(", ", missingServiceIds.stream().map(String::valueOf).toList())));

        Company company = companyService.getOne(reservation.getCompanyId());
        Preconditions.checkArgument(company != null, String.format("Company could not be found with [id=%s]", reservation.getCompanyId()));
        com.kokusz19.udinfopark.model.dao.Company companyDao = convert(company);

        return new com.kokusz19.udinfopark.model.dao.Reservation(reservation.getReservationId(),
                companyDao,
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
        Company companyDto = companyService.getOne(service.getCompanyId());
        Preconditions.checkArgument(companyDto != null, String.format("Company not found with [id=%d]", service.getCompanyId()));

        com.kokusz19.udinfopark.model.dao.Company company = convert(companyDto);

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
