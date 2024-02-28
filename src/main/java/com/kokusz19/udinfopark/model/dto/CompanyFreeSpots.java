package com.kokusz19.udinfopark.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CompanyFreeSpots {
    private int companyId;
    private List<ServiceFreeSpots> services;
}
