package com.kokusz19.udinfopark.api;

import com.kokusz19.udinfopark.model.dto.Reservation;
import com.kokusz19.udinfopark.model.dto.ReservationSearchParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("reservations")
public interface ReservationApi extends CrudApi<Reservation> {

    @PostMapping("search")
    List<Reservation> search(
            @RequestBody ReservationSearchParams searchParams);

}
