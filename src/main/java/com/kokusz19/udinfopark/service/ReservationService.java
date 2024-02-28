package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.model.dto.*;
import com.kokusz19.udinfopark.repository.ReservationRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

// TODO: Exception messages to be forwarded to caller

@Service
public class ReservationService implements ReservationApi {

    private static final int MINIMUM_MINUTE_DIFFERENCE_BETWEEN_SINGLE_SERVICE_RESERVATIONS = 0;
    private static final int MINIMUM_MINUTE_DIFFERENCE_BETWEEN_MULTIPLE_SERVICE_RESERVATIONS = 5;
    private final ReservationRepository reservationRepository;
    private final ServiceService serviceService;
    private final CompanyService companyService;
    private final ModelConverter modelConverter;
    private final ServiceReservationService serviceReservationService;

    public ReservationService(ReservationRepository reservationRepository, ServiceService serviceService, CompanyService companyService, ModelConverter modelConverter, ServiceReservationService serviceReservationService) {
        this.reservationRepository = reservationRepository;
        this.serviceService = serviceService;
        this.companyService = companyService;
        this.modelConverter = modelConverter;
        this.serviceReservationService = serviceReservationService;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll().stream().map(modelConverter::convert).toList();
    }

    @Override
    public Reservation getOne(int id) {
        return reservationRepository.findById(id).map(modelConverter::convert).orElse(null);
    }

    @Override
    public int create(Reservation subject) {
        validateServiceReservations(subject);

        List<com.kokusz19.udinfopark.model.dto.ServiceReservation> list = subject.getServiceReservations().stream()
                .map(modelConverter::convert)
                .map(serviceReservationService::save)
                .map(modelConverter::convert).toList();
        subject.setServiceReservations(list);

        return reservationRepository.save(modelConverter.convert(subject)).getReservationId();
    }


    private void validateServiceReservations(Reservation subject) {
        Company company = companyService.getOne(subject.getCompanyId());
        Preconditions.checkNotNull(company, String.format("Company not found [id=%d]", subject.getCompanyId()));

        List<com.kokusz19.udinfopark.model.dto.Service> companyServices = serviceService.getByCompanyId(subject.getCompanyId());
        Preconditions.checkArgument(!companyServices.isEmpty(), "There are no services under this company!");

        Map<Integer, com.kokusz19.udinfopark.model.dto.Service> services = new HashMap<>();

        boolean noOverlappingReservations = subject.getServiceReservations().stream()
                .map(serviceReservation -> {
                    com.kokusz19.udinfopark.model.dto.Service service = serviceService.getOne(serviceReservation.getServiceId());
                    services.put(service.getServiceId(), service);
                    return serviceReservationService.findByServiceIdAndDate(service, serviceReservation);
                })
                .map(Optional::isEmpty)
                .reduce(Boolean::logicalAnd)
                .orElse(true);

        Preconditions.checkState(noOverlappingReservations, "Could not make the reservation, because one or more timewindows have already been booked or are overlapping with others.");

        List<ServiceReservation> serviceReservations = subject.getServiceReservations().stream()
                .sorted((a, b) -> a.getReservationStart().after(b.getReservationStart()) ? 1 : -1)
                .toList();
        for (int i = 0; i < serviceReservations.size() - 1; i++) {
            Date serviceReservationAEnd = DateUtils.addMinutes(serviceReservations.get(i).getReservationStart(), services.get(serviceReservations.get(i).getServiceId()).getDurationMinutes());
            Date serviceReservationBStart = serviceReservations.get(i+1).getReservationStart();

            int minMinuteDiff = companyServices.size() == 1
                    ? MINIMUM_MINUTE_DIFFERENCE_BETWEEN_SINGLE_SERVICE_RESERVATIONS
                    : MINIMUM_MINUTE_DIFFERENCE_BETWEEN_MULTIPLE_SERVICE_RESERVATIONS;

            Preconditions.checkArgument(
                    serviceReservationBStart.getTime() >= DateUtils.addMinutes(serviceReservationAEnd, minMinuteDiff).getTime(),
                    String.format("There should be at least %d minutes of difference between service reservations [reservationA=%s] [reservationB=%s]", minMinuteDiff, serviceReservations.get(i).getReservationStart().toInstant().toString(), serviceReservations.get(i+1).getReservationStart().toInstant().toString()));
        }

        ServiceReservation lastReservation = serviceReservations.get(serviceReservations.size()-1);
        Date lastReservationEnd = DateUtils.addMinutes(lastReservation.getReservationStart(), services.get(lastReservation.getServiceId()).getDurationMinutes());
        Preconditions.checkArgument(lastReservation.getReservationStart().getDay() == lastReservationEnd.getDay(), "Reservation can't overflow to next day!");

        Date companyOpenTime = new Date(serviceReservations.get(0).getReservationStart().getTime());
        companyOpenTime.setHours(company.getOpenAt().getHour());
        companyOpenTime.setMinutes(company.getOpenAt().getMinute());
        Preconditions.checkArgument(companyOpenTime.getTime() <= serviceReservations.get(0).getReservationStart().getTime(), "Reservation can't underflow the open time!");

        Date companyCloseTime = new Date(lastReservationEnd.getTime());
        companyCloseTime.setHours(company.getCloseAt().getHour());
        companyCloseTime.setMinutes(company.getCloseAt().getMinute());
        Preconditions.checkArgument(companyCloseTime.getTime() >= lastReservationEnd.getTime(), "Reservation can't overflow the close time!");
    }

    @Override
    public Reservation update(int id, Reservation subject) {
        subject.setReservationId(id);
        // TODO: Are the new values (dates) available?
        return modelConverter.convert(reservationRepository.save(modelConverter.convert(subject)));
    }

    @Override
    public boolean delete(int id) {
        return reservationRepository.findById(id).map(company -> {
            reservationRepository.delete(company);
            return true;
        }).orElse(false);
    }


    @Override
    public List<CompanyFreeSpots> search(ReservationSearchParams searchParams) {
        if(searchParams == null || (searchParams.getOnDate() == null && searchParams.getFromDate() == null && searchParams.getToDate() == null)) {
            throw new IllegalArgumentException("Please pass in a search filter");
        }

        List<Company> companies = companyService.getAll();
        List<CompanyFreeSpots> companyFreeSpots = new ArrayList<>();

        for (Company company : companies) {

            List<com.kokusz19.udinfopark.model.dto.Service> services = serviceService.getByCompanyId(company.getCompanyId());

            List<ServiceFreeSpots> serviceFreeSpots = new ArrayList<>();
            for(com.kokusz19.udinfopark.model.dto.Service service : services) {

                Date fromDate = searchParams.getOnDate() != null ? new Date(searchParams.getOnDate().getYear()-1900, searchParams.getOnDate().getMonthValue()-1, searchParams.getOnDate().getDayOfMonth(), 0, 0, 0) : searchParams.getFromDate();
                Date toDate = searchParams.getOnDate() != null ? new Date(fromDate.getYear(), fromDate.getMonth(), fromDate.getDate(), 23, 59, 59) : searchParams.getToDate();
                List<ServiceReservation> serviceReservations = serviceReservationService.getByDates(service.getServiceId(), fromDate, toDate);

                List<Date> freeSpot = new ArrayList<>();
                for (Date currentDate = fromDate; currentDate.before(toDate); currentDate = DateUtils.addMinutes(currentDate,30)) {

                    if(!isCompanyOpen(company.getOpenAt(), company.getCloseAt(), currentDate)) {
                        continue;
                    }

                    Date finalCurrentDate = currentDate;
                    Boolean noCollision = serviceReservations.stream()
                            .map(serviceReservation ->
                                    finalCurrentDate.getTime() <= (DateUtils.addMinutes(serviceReservation.getReservationStart(), -service.getDurationMinutes()).getTime()) ||
                                            finalCurrentDate.getTime() >= DateUtils.addMinutes(serviceReservation.getReservationStart(), service.getDurationMinutes()-1).getTime())
                            .reduce(Boolean::logicalAnd)
                            .orElse(true);

                    if(noCollision) {
                        freeSpot.add(currentDate);
                    }
                }
                serviceFreeSpots.add(new ServiceFreeSpots(service.getServiceId(), freeSpot));
            }
            companyFreeSpots.add(new CompanyFreeSpots(company.getCompanyId(), serviceFreeSpots));
        }

        return companyFreeSpots;
    }

    private boolean isCompanyOpen(Time openAt, Time closeAt, Date currentDate) {
        int currentHours = DateUtils.addMinutes(currentDate, currentDate.getTimezoneOffset()).getHours();

        // The opening hour is strictly lower or the closing hour is strictly higher, than the current one
        if (openAt.getHour() > currentHours || closeAt.getHour() < currentHours) {
            return false;
        }

        // The opening hour is strictly higher and the closing hour is strictly lower, than the current one
        else if (openAt.getHour() < currentHours && closeAt.getHour() > currentHours) {
            return true;
        }

        // Opening hour and minute == OPEN
        // Closing hour and minute == CLOSED
        else if ((openAt.getHour() == currentHours && openAt.getMinute() > currentDate.getMinutes()) ||
                (closeAt.getHour() == currentHours && closeAt.getMinute() <= currentDate.getMinutes())) {
            return false;
        }

        return true;
    }
}
