package com.kokusz19.udinfopark.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int serviceId;
    @NotNull @Size(min = 2)
    private String name;
    @Min(1)
    private int companyId;
    private String description;
    @Min(0)
    private int durationMinutes;

}
