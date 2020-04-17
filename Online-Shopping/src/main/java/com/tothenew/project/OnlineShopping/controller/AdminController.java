package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.services.AdminDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminDaoService adminDaoService;

    @GetMapping("/admin/home")
    public String adminHome(){
        return "Welcome Admin To Online Shopping Portal";
    }

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
