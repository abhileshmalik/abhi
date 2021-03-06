package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.CustomerRegisterModel;
import com.tothenew.project.OnlineShopping.model.CustomerUpdateModel;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.model.UpdatePasswordModel;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import io.swagger.annotations.ApiOperation;
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


    @GetMapping("/")
    public String index(){
        return "Welcome To The Pro-Cart";
    }


    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
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

    ///////////////// Admin API's Part //////////////////////////////

    @ApiOperation(value = "Admin Module to Retrieve List of all Customers")
    @GetMapping("/customers")
    public MappingJacksonValue retrieveAllCustomers(@RequestHeader(defaultValue = "0") String page, @RequestHeader(defaultValue = "10")String size) {
       // logger.error("Error Message");
        return userDaoService.findAllCustomers(page, size);
    }

    @ApiOperation(value = "Admin Module to Retrieve List of all Sellers")
    @GetMapping("/sellers")
    public MappingJacksonValue retrieveAllSellers(@RequestHeader(defaultValue = "0") String page, @RequestHeader(defaultValue = "10")String size) {
        return userDaoService.findAllSellers(page, size);
    }

    @ApiOperation(value = "Admin Module to Enable a Seller Account")
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

    @ApiOperation(value = "API to register new Customers")
    @PostMapping(path = "/customerregistration")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRegisterModel customerRegisterModel) {
        String message = userDaoService.saveNewCustomer(customerRegisterModel);
        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }


    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        return userDaoService.confirmUserAccount(confirmationToken);
    }


    @ApiOperation(value = "Customer Home Page which supports Internationalized Content")
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

    @ApiOperation(value = "View Customer Profile")
    @GetMapping("/customer/home/profile")
    public MappingJacksonValue customerprofileview() {

        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("email","firstName","middleName",
                "lastName","username", "contact","isActive","isNonLocked");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter1);
        MappingJacksonValue mapping1 = new MappingJacksonValue(customerProfile());
        mapping1.setFilters(filterProvider1);

        return mapping1;
    }

    @ApiOperation(value = "View Customer Addresses")
    @GetMapping("/customer/home/profile/address")
    public MappingJacksonValue customeraddressview() {
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        FilterProvider filterProvider2 = new SimpleFilterProvider().addFilter("userfilter",filter2);
        MappingJacksonValue mapping2 = new MappingJacksonValue(customerProfile());
        mapping2.setFilters(filterProvider2);
        return mapping2;
    }

    @ApiOperation(value = "Update a customer Profile")
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

    @ApiOperation(value = "Add an address")
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

    @ApiOperation(value = "Delete an Address of Customer")
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

    @ApiOperation(value = "Update a customer Address")
    @PutMapping("/customer/updateAddress/{address_id}")
    public String updateCustomerAddress(@RequestBody AddressModel addressModel, @PathVariable Long address_id, HttpServletResponse response)
    {
        Customer customer = userDaoService.getLoggedInCustomer();
        Long user_id = customer.getUser_id();

        String message = userDaoService.updateAddress(addressModel,address_id, user_id);
        if (!message.equals("Address updated")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }


    @PostMapping("/resendactToken")
    public String resendActivationToken(@RequestBody String email) {

        String message = userDaoService.resendActivationToken(email);

        return message;

    }

    @ApiOperation(value = "Update the login password")
    @PostMapping("/customer/updatePassword")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody UpdatePasswordModel updatePasswordModel) {
        Customer customer = userDaoService.getLoggedInCustomer();
        String username = customer.getUsername();

        String message = userDaoService.updateCustomerPassword(updatePasswordModel,username);

        return new ResponseEntity<>(message, HttpStatus.OK);

    }



}
