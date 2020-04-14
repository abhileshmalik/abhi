package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.orderprocessing.Orders;
import com.tothenew.project.OnlineShopping.services.OrderDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderDaoService orderDaoService;

    @PostMapping("{customer_user_id}/order/{cart_id}")
    public void addToOrder(@PathVariable Long customer_user_id, @RequestBody Orders orders, @PathVariable Long cart_id){
        Orders orders1= orderDaoService.addToOrder(customer_user_id, orders, cart_id);
    }
}
