package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.ReservationApi;
import com.kokusz19.udinfopark.model.Reservation;
import com.kokusz19.udinfopark.model.ReservationSearchParams;
import com.kokusz19.udinfopark.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService implements ReservationApi {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getOne(int id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public int create(Reservation subject) {
        // TODO: Fix after added reservation times
        Optional<Reservation> byName = reservationRepository.findByreservorNameAndServices(subject.getReservorName(), subject.getServices());
        if(byName.isPresent()) {
            throw new RuntimeException("Reservation already exists!");
        }
        return reservationRepository.save(subject).getReservationId();
    }

    @Override
    public Reservation update(int id, Reservation subject) {
        subject.setReservationId(id);
        // TODO: Are the new values (dates) available?
        return reservationRepository.save(subject);
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

        if(searchParams.getOnDate() != null && searchParams.getFromDate() != null && searchParams.getToDate() != null) {
            throw new RuntimeException("You can only pass in either the OnDate or the FromDate and ToDate!");
        }

        // TODO
        Reservation e1 = new Reservation();
        e1.setReservationId(123);
        return List.of(e1);
    }
}
