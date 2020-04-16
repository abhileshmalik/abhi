package com.tothenew.project.OnlineShopping.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidation implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone phone) { }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext constraintValidatorContext) {

        if(phoneField == null) {
            return false;
        }
        if(phoneField.matches("^[0-9]*$"))
            return true;
        return false;
    }

}
