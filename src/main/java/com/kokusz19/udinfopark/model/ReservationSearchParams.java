package com.kokusz19.udinfopark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSearchParams {
    private LocalDate onDate;
    private Date fromDate;
    private Date toDate;
}
