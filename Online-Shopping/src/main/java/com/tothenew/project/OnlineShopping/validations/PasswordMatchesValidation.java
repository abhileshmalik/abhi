package com.tothenew.project.OnlineShopping.validations;


import com.tothenew.project.OnlineShopping.model.CustomerRegisterModel;
import com.tothenew.project.OnlineShopping.model.ForgotPasswordModel;
import com.tothenew.project.OnlineShopping.model.SellerRegisterModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidation
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){

        if(obj instanceof SellerRegisterModel){
            SellerRegisterModel seller = (SellerRegisterModel) obj;
            return seller.getPassword().equals(seller.getConfirmPassword());
        }
        else if(obj instanceof ForgotPasswordModel){
            ForgotPasswordModel passwords = (ForgotPasswordModel) obj;
            return passwords.getPassword().equals(passwords.getConfirmPassword());
        }
        else{
            CustomerRegisterModel customer = (CustomerRegisterModel) obj;
            return customer.getPassword().equals(customer.getConfirmPassword());
        }
    }
}
