package com.kokusz19.udinfopark.model.dao;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Time {
    @NotNull @Min(0) @Max(23)
    private int hour;
    @NotNull @Min(0) @Max(59)
    private int minute;
}
