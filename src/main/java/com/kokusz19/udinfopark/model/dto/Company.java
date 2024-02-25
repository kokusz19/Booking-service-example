package com.kokusz19.udinfopark.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int companyId;
    @NotNull @Size(min = 2)
    private String name;
    @NotNull @Size(min = 4)
    private String address;

    @NotNull @Valid
    private Time openAt;
    @NotNull @Valid
    private Time closeAt;
}
