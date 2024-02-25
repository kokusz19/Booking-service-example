package com.kokusz19.udinfopark.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    @Min(0) @Max(23)
    private int hour;
    @Min(0) @Max(59)
    private int minute;
}
