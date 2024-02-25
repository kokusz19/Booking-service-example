package com.kokusz19.udinfopark.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
