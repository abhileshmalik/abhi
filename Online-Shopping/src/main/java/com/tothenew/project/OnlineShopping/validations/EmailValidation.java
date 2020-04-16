package com.tothenew.project.OnlineShopping.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidation implements ConstraintValidator<Email, String> {
    @Override
    public void initialize(Email email) { }

    @Override
    public boolean isValid(String emailField, ConstraintValidatorContext constraintValidatorContext) {
        if(emailField == null) {
            return false;
        }
        if(emailField.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            return true;
        return false;
    }

}
