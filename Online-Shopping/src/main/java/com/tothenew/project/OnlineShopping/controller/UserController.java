package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.CustomerRegisterModel;
import com.tothenew.project.OnlineShopping.model.CustomerUpdateModel;
import com.tothenew.project.OnlineShopping.entities.AppUser;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private MessageSource messageSource;

    AppUser appUser;


    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    ///////////////// Admin API's Part //////////////////////////////

    @GetMapping("/customers")
    public MappingJacksonValue retrieveAllCustomers(@RequestHeader(defaultValue = "0") String page, @RequestHeader(defaultValue = "10")String size) {
        return userDaoService.findAllCustomers(page, size);
    }

    @GetMapping("/sellers")
    public MappingJacksonValue retrieveAllSellers(@RequestHeader(defaultValue = "0") String page, @RequestHeader(defaultValue = "10")String size) {
        return userDaoService.findAllSellers(page, size);
    }

    @PostMapping(path = "/admin/enableSeller/{sellerId}")
    public String enableSellerAccount(@PathVariable Long sellerId){
        return userDaoService.enableSellerAccount(sellerId);
    }



/*    @PostMapping(path = "/registration")
    public ResponseEntity<Object> createUser(@RequestBody User user){
        User newUser = userDaoService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }*/


 //////////////// Customer APIs ////////////////////////////

    @PostMapping(path = "/customerregistration")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRegisterModel customerRegisterModel) {
        String message = userDaoService.saveNewCustomer(customerRegisterModel);
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
        Customer customer = userDaoService.getLoggedInCustomer();
        String name = customer.getFirstName();
        return messageSource.getMessage("welcome.message",new Object[]{name},LocaleContextHolder.getLocale());

        //return "Welcome Customer To Online Shopping Portal";
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
        MappingJacksonValue mapping1 = new MappingJacksonValue(customerProfile());
        mapping1.setFilters(filterProvider1);

        return mapping1;
    }

    @GetMapping("/customer/home/profile/address")
    public MappingJacksonValue customeraddressview() {
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        FilterProvider filterProvider2 = new SimpleFilterProvider().addFilter("userfilter",filter2);
        MappingJacksonValue mapping2 = new MappingJacksonValue(customerProfile());
        mapping2.setFilters(filterProvider2);
        return mapping2;
    }


    @PatchMapping ("/customer/updateProfile")
    public String updateCustomerDetails(@RequestBody CustomerUpdateModel customerUpdateModel, HttpServletResponse response){
        Customer customer1 = userDaoService.getLoggedInCustomer();
        Long id = customer1.getUser_id();

        String message = userDaoService.updateCustomer(customerUpdateModel,id);
        if (!message.equals("Profile updated successfully")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Error";
        }
        else
            return message;
    }


    @PostMapping("/customer/addAddress")
    public String addCustomerAddress(@RequestBody AddressModel addressModel, HttpServletResponse response)
    {
        Customer customer1 = userDaoService.getLoggedInCustomer();
        Long id = customer1.getUser_id();

        String message = userDaoService.addAddress(addressModel,id);
        if (!message.equals("Address added")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @DeleteMapping("/customer/deleteAddress/{address_id}")
    public String deleteCustomerAddress(@PathVariable Long address_id, HttpServletResponse response)
    {
        Customer customer1 = userDaoService.getLoggedInCustomer();
        Long user_id = customer1.getUser_id();

        String message = userDaoService.deleteAddress(address_id, user_id);
        if (!message.equals("Address deleted")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }

    @PutMapping("/customer/updateAddress/{address_id}")
    public String updateCustomerAddress(@RequestBody AddressModel addressModel, @PathVariable Long address_id, HttpServletResponse response)
    {
        Customer customer1 = userDaoService.getLoggedInCustomer();

        String message = userDaoService.updateAddress(addressModel,address_id);
        if (!message.equals("Address updated")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }



}
