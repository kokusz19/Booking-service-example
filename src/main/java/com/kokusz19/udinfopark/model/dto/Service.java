package com.kokusz19.udinfopark.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int serviceId;
    @NotNull
    private String name;
    @NotNull
    private int companyId;
    private String description;
    @NotNull @Min(0)
    private int durationMinutes;

}
