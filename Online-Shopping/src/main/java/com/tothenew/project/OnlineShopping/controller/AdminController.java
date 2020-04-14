package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.services.AdminDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private AdminDaoService adminDaoService;

    @PatchMapping("/admin/activateuser/{uid}")
    public String userActivation(@PathVariable Long uid) {
        String message = adminDaoService.activateUser(uid);
        return  message;
    }

    @PatchMapping("/admin/deactivateuser/{uid}")
    public String userDeactivation(@PathVariable Long uid) {
        String message = adminDaoService.deactivateUser(uid);
        return  message;
    }

}
