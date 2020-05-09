package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.services.AccountUnlockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Account Unlocking APIs")
@RestController
public class AccountUnlockController {

    @Autowired
    private AccountUnlockService accountUnlockService;

    @ApiOperation(value = "Api where user will enter username for sending the unlocking code")
    @GetMapping("/account-unlock/{username}")
    public String unlockAccount(@PathVariable String username){
        String message = accountUnlockService.unlockAccount(username);
        return message;
    }

    @ApiOperation(value = "This API will be triggered from the link sent on user's email and unlock his account")
    @GetMapping("/do-unlock")
    public String unlockAccountSuccess(@RequestParam("username") String username)
    {
        String message = accountUnlockService.unlockAccountSuccess(username);
        return message;
    }
}
