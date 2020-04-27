package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.model.ForgotPasswordModel;
import com.tothenew.project.OnlineShopping.services.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestBody String email){
        String message = forgotPasswordService.resetUserPassword(email);
        return message;
    }

    @PutMapping("/reset-password")
    public String setPassword(@RequestParam("token") String resetToken,@Valid @RequestBody ForgotPasswordModel forgotPasswordModel){
        String message =forgotPasswordService.updatePassword(resetToken, forgotPasswordModel);
        return message;
    }

}
