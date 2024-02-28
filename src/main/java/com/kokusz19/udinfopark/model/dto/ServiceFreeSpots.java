package com.kokusz19.udinfopark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ServiceFreeSpots {
    private int serviceId;
    private List<Date> freeSpots;
}
