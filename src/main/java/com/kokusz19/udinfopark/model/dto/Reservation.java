package com.kokusz19.udinfopark.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @NotNull
    private int reservationId;
    @Min(1)
    private int companyId;
    @NotEmpty
    private List<Integer> serviceIds;

    @NotEmpty
    private String reservorName;
    @NotEmpty
    private String reservorPhoneNumber;
    @NotEmpty
    private String reservorEmail;

}
