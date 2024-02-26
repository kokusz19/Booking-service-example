package com.kokusz19.udinfopark.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int reservationId;
    @Min(1)
    private int companyId;
    @NotEmpty @Size(min = 1) @Valid
    private List<ServiceReservation> serviceReservations;

    @NotEmpty @Size(min = 3)
    private String reservorName;
    @Pattern(regexp = "^[\\+]?[0-9]{0,3}?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
    private String reservorPhoneNumber;
    @NotEmpty @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String reservorEmail;

}
