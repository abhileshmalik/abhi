package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.model.ProductReviewModel;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.services.ProductReviewService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping("/addreview/{product_id}")
    public ResponseEntity<Object> addReview(@Valid @RequestBody ProductReviewModel productReviewModel, @PathVariable Long product_id){
        Customer customer = userDaoService.getLoggedInCustomer();
        Long customer_user_id = customer.getUser_id();

        String message = productReviewService.addReview(productReviewModel, customer_user_id, product_id);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


}
