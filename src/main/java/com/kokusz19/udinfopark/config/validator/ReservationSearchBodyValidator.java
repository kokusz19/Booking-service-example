package com.kokusz19.udinfopark.config.validator;

import com.kokusz19.udinfopark.model.dto.ReservationSearchParams;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservationSearchBodyValidator implements ConstraintValidator<ReservationSearchBody, ReservationSearchParams> {

    @Override
    public void initialize(ReservationSearchBody constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationSearchParams searchParams, ConstraintValidatorContext context) {
        if(searchParams.getOnDate() != null && searchParams.getFromDate() != null ||
                searchParams.getOnDate() != null && searchParams.getToDate() != null) {
            throw new RuntimeException("You can only pass in either the OnDate or the FromDate and ToDate!");
        }
        return true;
    }

}
