package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.model.ForgotPasswordModel;
import com.tothenew.project.OnlineShopping.services.ForgotPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value="Forgot Password APIs")
@RestController
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @ApiOperation(value = "Api where user will enter Email for sending the password reset code")
    @PostMapping("/forgot-password")
    public String resetPassword(@RequestBody String email){
        String message = forgotPasswordService.resetUserPassword(email);
        return message;
    }

    @ApiOperation(value = "This API will be triggered from the link sent on user's email and he can reset his password")
    @PutMapping("/reset-password")
    public String setPassword(@RequestParam("token") String resetToken,@Valid @RequestBody ForgotPasswordModel forgotPasswordModel){
        String message =forgotPasswordService.updatePassword(resetToken, forgotPasswordModel);
        return message;
    }

}
