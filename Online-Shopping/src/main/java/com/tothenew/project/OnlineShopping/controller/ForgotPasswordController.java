package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.dto.ForgotPasswordDto;
import com.tothenew.project.OnlineShopping.services.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String setPassword(@RequestParam("token") String resetToken, @RequestBody ForgotPasswordDto forgotPasswordDto){
        String message =forgotPasswordService.updatePassword(resetToken,forgotPasswordDto);
        return message;
    }

}
