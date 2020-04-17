package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.services.ProductVariationDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductVariationController {

    @Autowired
    private ProductVariationDaoService productVariationDaoService;


    @GetMapping("/product/variant/{variantId}")
    public ProductVariation viewVariant(@PathVariable Long variantId) {
        return productVariationDaoService.findVariant(variantId);
    }

   /* @GetMapping("/product/variant/{variantId}")
    public MappingJacksonValue retrieveVariant(@PathVariable Long variantId){
        SimpleBeanPropertyFilter filter4 = SimpleBeanPropertyFilter.filterOutAllExcept("variant_name",
                "quantity_available","price");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("variantFilter",filter4);

        MappingJacksonValue mapping=new MappingJacksonValue(viewVariant(variantId));
        mapping.setFilters(filterProvider);

        return mapping;

    }*/


}