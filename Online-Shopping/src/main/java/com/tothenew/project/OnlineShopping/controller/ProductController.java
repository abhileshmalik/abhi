package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
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

        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("productfilter",filter1);

        MappingJacksonValue mapping = new MappingJacksonValue(findCategorywiseProducts(category_name));

        mapping.setFilters(filterProvider1);

        return mapping;
    }


    @PostMapping("/save-product/category/{category_name}")
    public void saveProduct(@RequestBody List<Product> products, @PathVariable String category_name){
        Seller seller = userDaoService.getLoggedInSeller();
        Long seller_user_id = seller.getUser_id();
        List<Product> product1= productDaoService.addNewProduct(seller_user_id, products, category_name);
    }


    @GetMapping("/product/{product_id}")
    public MappingJacksonValue retrieveProduct(@PathVariable Long product_id) {
        SimpleBeanPropertyFilter filter3 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productfilter",filter3);

        MappingJacksonValue mapping1=new MappingJacksonValue(viewProduct(product_id));
        mapping1.setFilters(filterProvider);

        return mapping1;
    }

    public Product viewProduct(Long product_id) {
        return productDaoService.findProduct(product_id);
    }


}
