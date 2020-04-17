package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.services.CategoryDaoService;
import com.tothenew.project.OnlineShopping.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryDaoService categoryDaoService;

    //@GetMapping("/category")
    public List<Category> retrieveAllCategories() {
        return categoryDaoService.findAll();
    }

    public List<Category> retrieveAllSubCategories() {
        return categoryDaoService.findSubCategories();
    }

    @GetMapping("/productcategories")
    public MappingJacksonValue retrieveCategoryList() {
        SimpleBeanPropertyFilter filter5 = SimpleBeanPropertyFilter.filterOutAllExcept("name");
        FilterProvider filterProvider5 = new SimpleFilterProvider().addFilter("categoryfilter",filter5);

        MappingJacksonValue mapping5=new MappingJacksonValue(retrieveAllSubCategories());
        mapping5.setFilters(filterProvider5);

        return mapping5;
    }

    @PostMapping("/add-category")
    public ResponseEntity<Object> saveCategory(@RequestBody Category category){
        String message= categoryDaoService.saveNewCategory(category);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/add-category/{parentCategory}")
    public ResponseEntity<Object> saveSubCategory(@PathVariable String parentCategory, @RequestBody List<Category> subCategory){
        String message = categoryDaoService.saveNewSubCategory(parentCategory, subCategory);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


}