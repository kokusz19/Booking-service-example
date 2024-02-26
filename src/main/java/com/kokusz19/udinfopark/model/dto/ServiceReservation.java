package com.kokusz19.udinfopark.model.dto;

import com.kokusz19.udinfopark.config.validator.ServiceReservationDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservation {
    private int serviceReservationId;
    @Min(1)
    private int serviceId;
    @NotNull @ServiceReservationDate
    private Date reservationStart;

    public ServiceReservation(int serviceId, Date reservationStart) {
        this.serviceId = serviceId;
        this.reservationStart = reservationStart;
    }
}
