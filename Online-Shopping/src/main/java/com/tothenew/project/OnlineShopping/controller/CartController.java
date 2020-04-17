package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.services.CartDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartDaoService cartDaoService;

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping("/add-to-cart/{productVariation_id}")
    public ResponseEntity<Object> addToCart(@RequestBody Cart cart, @PathVariable Long productVariation_id) {
        Customer customer = userDaoService.getLoggedInCustomer();
        Long customer_user_id = customer.getUser_id();

        Cart cart1= cartDaoService.addToCart(cart, customer_user_id, productVariation_id);

        return new ResponseEntity<>(cart1, HttpStatus.CREATED);
    }
}
