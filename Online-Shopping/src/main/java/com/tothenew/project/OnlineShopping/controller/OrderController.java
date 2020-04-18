package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.orderprocessing.Orders;
import com.tothenew.project.OnlineShopping.services.OrderDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderDaoService orderDaoService;

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping("/order/{cart_id}")
    public ResponseEntity<Object> addToOrder(@RequestBody Orders orders, @PathVariable Long cart_id){
        Customer customer = userDaoService.getLoggedInCustomer();
        Long customer_user_id = customer.getUser_id();

        String message = orderDaoService.addToOrder(orders, customer_user_id, cart_id);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
