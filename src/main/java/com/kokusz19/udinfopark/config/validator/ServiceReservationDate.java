package com.kokusz19.udinfopark.config.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ServiceReservationDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceReservationDate {
    String message() default "Bookings can only be done for either whole hours and half hours!";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
