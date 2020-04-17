package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductDaoService productDaoService;

    @Autowired
    private UserDaoService userDaoService;


    public List<Product> retrieveAllProducts () {
        return productDaoService.findAll();
    }

    public List<Product> findCategorywiseProducts(String category_name){
        return productDaoService.findCategoryProducts(category_name);
    }

    @GetMapping("/products/{category_name}")
    public MappingJacksonValue retrieveProductList(@PathVariable String category_name) {

        SimpleBeanPropertyFilter filter6 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider7 = new SimpleFilterProvider().addFilter("productfilter",filter6);

        MappingJacksonValue mapping6 = new MappingJacksonValue(findCategorywiseProducts(category_name));

        mapping6.setFilters(filterProvider7);

        return mapping6;
    }


    @PostMapping("/save-product/category/{category_name}")
    public ResponseEntity<Object> saveProduct(@RequestBody List<Product> products, @PathVariable String category_name){
        Seller seller = userDaoService.getLoggedInSeller();
        Long seller_user_id = seller.getUser_id();
        String message = productDaoService.addNewProduct(seller_user_id, products, category_name);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



    @GetMapping("/product/{product_id}")
    public MappingJacksonValue retrieveProduct(@PathVariable Long product_id) {
        SimpleBeanPropertyFilter filter7 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider7 = new SimpleFilterProvider().addFilter("productfilter",filter7);

        MappingJacksonValue mapping7=new MappingJacksonValue(viewProduct(product_id));
        mapping7.setFilters(filterProvider7);

        return mapping7;
    }

    public Product viewProduct(Long product_id) {
        return productDaoService.findProduct(product_id);
    }


}
