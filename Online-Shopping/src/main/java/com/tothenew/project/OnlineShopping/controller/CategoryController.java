package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.entities.CategoryMetadataField;
import com.tothenew.project.OnlineShopping.model.CategoryModel;
import com.tothenew.project.OnlineShopping.model.MetadataFieldValueInsertModel;
import com.tothenew.project.OnlineShopping.services.CategoryDaoService;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.services.CategoryMetadataFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryDaoService categoryDaoService;

    @Autowired
    private CategoryMetadataFieldService categoryMetadataFieldService;

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
    public ResponseEntity<Object> saveCategory(@Valid @RequestBody CategoryModel categoryModel){
        String message= categoryDaoService.saveNewCategory(categoryModel);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/add-category/{parentCategory}")
    public ResponseEntity<Object> saveSubCategory(@PathVariable String parentCategory, @RequestBody List<CategoryModel> subCategories){
        String message = categoryDaoService.saveNewSubCategory(parentCategory, subCategories);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping ("/updateCategory/{category}")
    public String updateCategory(@RequestBody CategoryModel categoryModel, @PathVariable String category){
        return categoryDaoService.updateCategory(categoryModel, category);
    }

    @PostMapping("/metadata-fields/add")
    public String addMetaDataField(@RequestParam String fieldName) {
        return categoryMetadataFieldService.addNewMetadataField(fieldName);
    }

    @GetMapping("/allmetadatafields")
    public List<CategoryMetadataField> findAllMetadataFields(){
        return categoryMetadataFieldService.findAllMetadataFields();
    }


    @PostMapping("/metadata-fields/addValues/{categoryId}/{metaFieldId}")
    public String addMetaDataFieldValues(@RequestBody MetadataFieldValueInsertModel fieldValueDtos, @PathVariable Long categoryId, @PathVariable Long metaFieldId) {
        return categoryMetadataFieldService.addNewMetadataFieldValues(fieldValueDtos, categoryId, metaFieldId);
    }

}