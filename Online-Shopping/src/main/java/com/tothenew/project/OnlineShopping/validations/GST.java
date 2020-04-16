package com.tothenew.project.OnlineShopping.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = GSTValidation.class)
@Documented
public @interface GST {
    String message() default "Invalid GSTIN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}