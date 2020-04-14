package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.services.CartDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartDaoService cartDaoService;

    @PostMapping("{customer_user_id}/add-to-cart/{productVariation_id}")
    public void addToCart(@PathVariable Long customer_user_id, @RequestBody Cart cart, @PathVariable Long productVariation_id){
        Cart cart1= cartDaoService.addToCart(customer_user_id, cart, productVariation_id);
    }
}
