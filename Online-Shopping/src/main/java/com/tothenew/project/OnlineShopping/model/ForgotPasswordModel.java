package com.tothenew.project.OnlineShopping.model;

import com.tothenew.project.OnlineShopping.validations.Password;
import com.tothenew.project.OnlineShopping.validations.PasswordMatches;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class ForgotPasswordModel {

    @NotNull
    @Password
    private String password;

    @NotNull
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
