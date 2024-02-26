package com.kokusz19.udinfopark.api;

import com.kokusz19.udinfopark.model.dto.ServiceReservation;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("service-reservations")
public interface ServiceReservationApi extends CrudApi<ServiceReservation> {
    @Override
    int create(ServiceReservation subject);
}
