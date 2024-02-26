package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.ReservationSearchParams;
import com.kokusz19.udinfopark.model.dto.ServiceReservation;
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
    private final ModelConverter modelConverter;
    private final ServiceReservationService serviceReservationService;

    public ReservationService(ReservationRepository reservationRepository, ServiceService serviceService, ModelConverter modelConverter, ServiceReservationService serviceReservationService) {
        this.reservationRepository = reservationRepository;
        this.serviceService = serviceService;
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
        List<com.kokusz19.udinfopark.model.dto.Service> companyServices = serviceService.getByCompanyId(subject.getCompanyId());
        Preconditions.checkArgument(!companyServices.isEmpty(), "There are no services under this company!");

        Map<Integer, com.kokusz19.udinfopark.model.dto.Service> services = new HashMap<>();

        boolean noOverlappingReservations = subject.getServiceReservations().stream()
                .map(serviceReservation -> {
                    com.kokusz19.udinfopark.model.dto.Service service = serviceService.getOne(serviceReservation.getServiceId());
                    services.put(service.getServiceId(), service);
                    return serviceReservationService.findByServiceIdAndDate(serviceReservation, service);
                })
                .map(Optional::isEmpty)
                .reduce(Boolean::logicalAnd)
                .orElse(true);
        // TODO: more detailed message
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
    public List<Reservation> search(ReservationSearchParams searchParams) {
        if(searchParams == null || (searchParams.getOnDate() == null && searchParams.getFromDate() == null && searchParams.getToDate() == null)) {
            return getAll();
        }

        // TODO
        Reservation e1 = new Reservation();
        e1.setReservationId(123);
        return List.of(e1);
    }
}
