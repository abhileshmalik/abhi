package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.dto.CustomerRegisterDto;
import com.tothenew.project.OnlineShopping.dto.SellerRegisterDto;
import com.tothenew.project.OnlineShopping.entities.AppUser;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private TokenStore tokenStore;

    AppUser appUser;


    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/customers")
    public List<Customer> retrieveAllCustomers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size) {
        return userDaoService.findAllCustomers(page, size);
    }

    @GetMapping("/sellers")
    public List<Seller> retrieveAllSellers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10")String size) {
        return userDaoService.findAllSellers(page, size);
    }



    @PostMapping(path = "/registration")
    public ResponseEntity<Object> createUser(@RequestBody User user){
        User newUser = userDaoService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(path = "/customerregistration")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto) {
        String message = userDaoService.saveNewCustomer(customerRegisterDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }


    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        String message= userDaoService.confirmUserAccount(confirmationToken);
        return message;
    }

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

    @GetMapping("/")
    public String index(){
        return "Welcome To Online Shopping Portal";
    }


    @GetMapping("/customer/home")
    public String customerHome(){
        return "Welcome Customer To Online Shopping Portal";
    }


    public Customer customerProfile(){
        Customer customer = userDaoService.getLoggedInCustomer();
        return customer;
    }

    @GetMapping("/customer/home/profile")
    public MappingJacksonValue customerprofileview() {

        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("email","firstName","middleName",
                "lastName","username", "contact","isActive","isNonLocked");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter1);
        MappingJacksonValue mapping = new MappingJacksonValue(customerProfile());
        mapping.setFilters(filterProvider1);

        return mapping;
    }

    @GetMapping("/customer/home/profile/address")
    public MappingJacksonValue customeraddressview() {
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter1);
        MappingJacksonValue mapping = new MappingJacksonValue(customerProfile());
        mapping.setFilters(filterProvider1);
        return mapping;
    }



}
