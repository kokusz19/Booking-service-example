package com.kokusz19.udinfopark.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @NotNull
    private int companyId;
    @NotNull
    private String name;
    @NotNull
    private String address;

    // TODO: TEST
    @NotNull @Valid
    private Time openAt;
    @NotNull
    private Time closeAt;
}
