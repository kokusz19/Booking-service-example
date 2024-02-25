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
    private final ModelConverter modelConverter;

    public ReservationService(ReservationRepository reservationRepository, ModelConverter modelConverter) {
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
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
        // TODO: Fix after added reservation times
        Optional<com.kokusz19.udinfopark.model.dao.Reservation> reservation = reservationRepository.findById(subject.getReservationId());
        Preconditions.checkArgument(reservation.isEmpty(), "Reservation already exists!");
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
