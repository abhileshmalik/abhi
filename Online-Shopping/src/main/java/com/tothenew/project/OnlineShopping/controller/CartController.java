package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.services.CartDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Cart/Wishlist APIs")
@RestController
public class CartController {

    @Autowired
    private CartDaoService cartDaoService;

    @Autowired
    private UserDaoService userDaoService;


    @ApiOperation(value = "API to add any product-variant to cart and define the quantity as well")
    @PostMapping("/add-to-cart/{productVariation_id}")
    public ResponseEntity<Object> addToCart(@RequestBody Cart cart, @PathVariable Long productVariation_id) {
        Customer customer = userDaoService.getLoggedInCustomer();
        Long customer_user_id = customer.getUser_id();

        String message = cartDaoService.addToCart(cart, customer_user_id, productVariation_id);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "API to add any product-variant to the wishlist")
    @PostMapping("/add-to-wishlist/{vid}")
    public ResponseEntity<Object> addToWishlist(@PathVariable Long vid) {

        Customer customer = userDaoService.getLoggedInCustomer();
        Long customer_id = customer.getUser_id();

        String message = cartDaoService.addtoWishlist(customer_id, vid);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


}
