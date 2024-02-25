package com.kokusz19.udinfopark.config.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ReservationSearchBodyValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReservationSearchBody {
    String message() default "You can only pass in either the OnDate or the FromDate and ToDate!";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
