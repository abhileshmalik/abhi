package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.services.ProductVariantService;
import com.tothenew.project.OnlineShopping.services.ProductVariationDaoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductVariationController {

    @Autowired
    private ProductVariationDaoService productVariationDaoService;

    @Autowired
    private ProductVariantService productVariantService;


    public ProductVariation viewVariant(@PathVariable Long variantId) {
        return productVariationDaoService.findVariant(variantId);
    }

    @ApiOperation(value = "Get a Product-Variant by Id")
    @GetMapping("/product/variant/{variantId}")
    public MappingJacksonValue retrieveVariant(@PathVariable Long variantId){
        SimpleBeanPropertyFilter filter4 = SimpleBeanPropertyFilter.filterOutAllExcept("variantName", "price",
                        "quantityAvailable","is_active", "productAttributes");

        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("variantfilter",filter4);

        MappingJacksonValue mapping1=new MappingJacksonValue(viewVariant(variantId));
        mapping1.setFilters(filterProvider1);

        return mapping1;

    }


    @GetMapping("/save-variant/{id}")
    public void save(@PathVariable Long id) {

        String message = productVariantService.saveVariant(id);
        System.out.println(message);

    }

    @GetMapping("/view-variant/{vid}")
    public ProductVariant view(@PathVariable String vid) {
        return productVariantService.viewVariant(vid);
    }




}
