package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.SellerRegisterModel;
import com.tothenew.project.OnlineShopping.model.SellerUpdateModel;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.services.SellerDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Autowired
    private MessageSource messageSource;

    @PostMapping(path = "/sellerregistration")
    public ResponseEntity<Object> createSeller(@Valid @RequestBody SellerRegisterModel sellerRegisterModel) {
        String message = userDaoService.saveNewSeller(sellerRegisterModel);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/seller/home")
    public String sellerHome(){
        Seller seller = userDaoService.getLoggedInSeller();
        String name = seller.getFirstName();
        return messageSource.getMessage("welcome.message",new Object[]{name}, LocaleContextHolder.getLocale());
    }


    public Seller sellerProfile(){
        Seller seller = userDaoService.getLoggedInSeller();
        return seller;
    }

    @GetMapping("/seller/home/profile")
    public MappingJacksonValue sellerprofileview() {

        SimpleBeanPropertyFilter filter8 = SimpleBeanPropertyFilter.filterOutAllExcept("email","firstName","middleName",
                "lastName","username", "companyName", "companyContact", "gstin", "isActive","isNonLocked");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter8);
        MappingJacksonValue mapping = new MappingJacksonValue(sellerProfile());
        mapping.setFilters(filterProvider1);

        return mapping;
    }

    @GetMapping("/seller/home/profile/address")
    public MappingJacksonValue selleraddressview() {
        SimpleBeanPropertyFilter filter9 = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("userfilter",filter9);
        MappingJacksonValue mapping = new MappingJacksonValue(sellerProfile());
        mapping.setFilters(filterProvider1);
        return mapping;
    }

    @PatchMapping("/seller/updateProfile")
    public String updateSellerDetails(@RequestBody SellerUpdateModel sellerUpdateModel, HttpServletResponse response){
        Seller seller= userDaoService.getLoggedInSeller();
        Long id = seller.getUser_id();

        String message = sellerDaoService.updateSeller(sellerUpdateModel,id);
        if (!message.equals("Profile updated successfully")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Error";
        }
        else
            return message;
    }

    @PutMapping("/seller/updateProfile/address/{address_id}")
    public String updateCustomerAddress(@RequestBody AddressModel addressModel, @PathVariable Long address_id, HttpServletResponse response)
    {
        Seller seller= userDaoService.getLoggedInSeller();
        Long sellerid = seller.getUser_id();

        String message = sellerDaoService.updateAddress(addressModel,address_id, sellerid);
        if (!message.equals("Address updated")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return message;
    }
}
