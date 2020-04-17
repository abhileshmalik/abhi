package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.dto.AddressDto;
import com.tothenew.project.OnlineShopping.dto.SellerRegisterDto;
import com.tothenew.project.OnlineShopping.dto.SellerUpdateDto;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.services.SellerDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class SellerController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private SellerDaoService sellerDaoService;


    @PostMapping(path = "/sellerregistration")
    public ResponseEntity<Object> createSeller(@Valid @RequestBody SellerRegisterDto sellerRegisterDto) {
        String message = userDaoService.saveNewSeller(sellerRegisterDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/seller/home")
    public String sellerHome(){
        return "Welcome Seller To Online Shopping Portal";
    }


    public Seller sellerProfile(){
        Seller seller = userDaoService.getLoggedInSeller();
        return seller;
    }

    @GetMapping("/seller/home/profile")
    public MappingJacksonValue sellerprofileview() {

        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("email","firstName","middleName",
                "lastName","username", "companyName", "companyContact", "gstin","isActive","isNonLocked");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter1);
        MappingJacksonValue mapping = new MappingJacksonValue(sellerProfile());
        mapping.setFilters(filterProvider1);

        return mapping;
    }

    @GetMapping("/seller/home/profile/address")
    public MappingJacksonValue selleraddressview() {
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter1);
        MappingJacksonValue mapping = new MappingJacksonValue(sellerProfile());
        mapping.setFilters(filterProvider1);
        return mapping;
    }

    @PatchMapping("/seller/updateProfile")
    public String updateSellerDetails(@RequestBody SellerUpdateDto sellerUpdateDto, HttpServletResponse response){
        Seller seller= userDaoService.getLoggedInSeller();
        Long id = seller.getUser_id();

        String message = sellerDaoService.updateSeller(sellerUpdateDto,id);
        if (!message.equals("Profile updated successfully")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Error";
        }
        else
            return message;
    }

    @PutMapping("/seller/updateProfile/address{address_id}")
    public String updateCustomerAddress(@RequestBody AddressDto addressDto, @PathVariable Long address_id, HttpServletResponse response)
    {
        Seller seller= userDaoService.getLoggedInSeller();

        String message = sellerDaoService.updateAddress(addressDto,address_id);
        if (!message.equals("Address updated")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }
}
