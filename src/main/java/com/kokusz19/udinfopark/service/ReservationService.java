package com.kokusz19.udinfopark.service;

import com.google.common.base.Preconditions;
import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.ReservationSearchParams;
import com.kokusz19.udinfopark.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService implements ReservationApi {

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
        boolean validReservation = subject.getServiceReservations().stream()
                .map(serviceReservation -> {
                    com.kokusz19.udinfopark.model.dto.Service service = serviceService.getOne(serviceReservation.getServiceId());
                    return serviceReservationService.findByServiceIdAndDate(serviceReservation, service);
                })
                .map(Optional::isEmpty)
                .reduce(Boolean::logicalAnd)
                .orElse(true);

        // TODO: more detailed message
        Preconditions.checkState(validReservation, "Could not make the reservation, because one or more timewindows have already been booked or are overlapping with others.");

        List<com.kokusz19.udinfopark.model.dto.ServiceReservation> list = subject.getServiceReservations().stream()
                .map(modelConverter::convert)
                .map(serviceReservationService::save)
                .map(modelConverter::convert).toList();
        subject.setServiceReservations(list);

        return reservationRepository.save(modelConverter.convert(subject)).getReservationId();
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
