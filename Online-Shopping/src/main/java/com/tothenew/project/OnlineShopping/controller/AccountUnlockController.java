package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.services.AccountUnlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountUnlockController {

    @Autowired
    private AccountUnlockService accountUnlockService;

    @GetMapping("/account-unlock/{username}")
    public String unlockAccount(@PathVariable String username){
        String message = accountUnlockService.unlockAccount(username);
        return message;
    }

    @GetMapping("/do-unlock")
    public String unlockAccountSuccess(@RequestParam("username") String username)
    {
        String message = accountUnlockService.unlockAccountSuccess(username);
        return message;
    }
}
