package com.kokusz19.udinfopark.config.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class ServiceReservationDateValidator implements ConstraintValidator<ServiceReservationDate, Date> {

    @Override
    public void initialize(ServiceReservationDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date reservationStart, ConstraintValidatorContext context) {
        if((reservationStart.getMinutes() != 0 && reservationStart.getMinutes() != 30) || reservationStart.getSeconds() != 0 || (reservationStart.getTime() % 1000) != 0) {
            throw new RuntimeException("Bookings can only be done for either whole hours and half hours [date="+reservationStart.toInstant().toString()+"]!");
        }
        return true;
    }

}
