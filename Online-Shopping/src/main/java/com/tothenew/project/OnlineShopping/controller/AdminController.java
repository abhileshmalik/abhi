package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.services.AdminDaoService;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Api(value="Admin Controller APIs")
@RestController
public class AdminController {

    @Autowired
    private AdminDaoService adminDaoService;

    @Autowired
    private ProductDaoService productDaoService;


    @ApiOperation(value = "Admin Home Page")
    @GetMapping("/admin/home")
    public String adminHome(){
        return "Welcome Admin To Online Shopping Portal";
    }


    @ApiOperation(value = "Admin Module to Activate a User")
    @PatchMapping("/admin/activateuser/{uid}")
    public String userActivation(@PathVariable Long uid) {
        String message = adminDaoService.activateUser(uid);
        return  message;
    }


    @ApiOperation(value = "Admin Module to Deactivate a User")
    @PatchMapping("/admin/deactivateuser/{uid}")
    public String userDeactivation(@PathVariable Long uid) {
        String message = adminDaoService.deactivateUser(uid);
        return  message;
    }

    @ApiOperation(value = "Admin Module to Activate a Product")
    @PatchMapping("/admin/activateproduct/{pid}")
    public String productActivation(@PathVariable Long pid) {
        String message = productDaoService.activateProduct(pid);
        return  message;
    }

    @ApiOperation(value = "Admin Module to Deactivate a Product")
    @PatchMapping("/admin/deactivateproduct/{pid}")
    public String productDeactivation(@PathVariable Long pid) {
        String message = productDaoService.deactivateProduct(pid);
        return  message;
    }




}
