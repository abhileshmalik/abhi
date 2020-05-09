package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.entities.CategoryMetadataField;
import com.tothenew.project.OnlineShopping.model.CategoryModel;
import com.tothenew.project.OnlineShopping.model.FilterCategoryModel;
import com.tothenew.project.OnlineShopping.model.MetadataFieldValueInsertModel;
import com.tothenew.project.OnlineShopping.services.CategoryDaoService;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.services.CategoryMetadataFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value="Category and Metadata fields Related APIs")
@RestController
public class CategoryController {

   // public static final  SimpleBeanPropertyFilter categorynamefilter =  SimpleBeanPropertyFilter.filterOutAllExcept("name");

    @Autowired
    private CategoryDaoService categoryDaoService;

    @Autowired
    private CategoryMetadataFieldService categoryMetadataFieldService;

    public List<Category> retrieveAllCategories() {
        return categoryDaoService.findAll();
    }

    @ApiOperation(value = "Enlist all categories and sub categories registered")
    @GetMapping("/allcategories")
    public MappingJacksonValue retrieveAllCategoryList() {
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("name","subcategory");
        FilterProvider filterProvider1 = new SimpleFilterProvider().addFilter("categoryfilter",filter1);

        MappingJacksonValue mapping1=new MappingJacksonValue(retrieveAllCategories());
        mapping1.setFilters(filterProvider1);

        return mapping1;
    }

    public List<Category> retrieveAllSubCategories() {
        return categoryDaoService.findSubCategories();
    }

    @ApiOperation(value = "Enlist all Sub-Categories")
    @GetMapping("/productcategories")
    public MappingJacksonValue retrieveCategoryList() {
        SimpleBeanPropertyFilter filter5 = SimpleBeanPropertyFilter.filterOutAllExcept("name");
        FilterProvider filterProvider5 = new SimpleFilterProvider().addFilter("categoryfilter",filter5);

        MappingJacksonValue mapping5=new MappingJacksonValue(retrieveAllSubCategories());
        mapping5.setFilters(filterProvider5);

        return mapping5;
    }

    @ApiOperation(value = "Add new Parent Category")
    @PostMapping("/add-category")
    public ResponseEntity<Object> saveCategory(@Valid @RequestBody CategoryModel categoryModel){
        String message= categoryDaoService.saveNewCategory(categoryModel);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add new Sub-Categories under given Parent Category")
    @PostMapping("/add-category/{parentCategory}")
    public ResponseEntity<Object> saveSubCategory(@PathVariable String parentCategory, @RequestBody List<CategoryModel> subCategories){
        String message = categoryDaoService.saveNewSubCategory(parentCategory, subCategories);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update any Category name")
    @PutMapping ("/updateCategory/{category}")
    public String updateCategory(@RequestBody CategoryModel categoryModel, @PathVariable String category){
        return categoryDaoService.updateCategory(categoryModel, category);
    }

    @ApiOperation(value = "Add metadata fields for a category")
    @PostMapping("/metadata-fields/add")
    public String addMetaDataField(@RequestParam String fieldName) {
        return categoryMetadataFieldService.addNewMetadataField(fieldName);
    }

    @ApiOperation(value = "View all Metadata fields")
    @GetMapping("/allmetadatafields")
    public List<CategoryMetadataField> findAllMetadataFields(){
        return categoryMetadataFieldService.findAllMetadataFields();
    }


    @ApiOperation(value = "Add metadata field values for a category")
    @PostMapping("/metadata-fields/addValues/{categoryId}/{metaFieldId}")
    public String addMetaDataFieldValues(@RequestBody MetadataFieldValueInsertModel fieldValueDtos, @PathVariable Long categoryId, @PathVariable Long metaFieldId) {
        return categoryMetadataFieldService.addNewMetadataFieldValues(fieldValueDtos, categoryId, metaFieldId);
    }


    public List<Category> viewChildCategories(Long categoryid) {
        return categoryDaoService.viewChildCat(categoryid);
    }

    @ApiOperation(value = "View all Leaf categories for a same parent category")
    @GetMapping("/subcategies/{categoryid}")
    public MappingJacksonValue viewLeafCategories(@PathVariable Long categoryid) {
        SimpleBeanPropertyFilter filter5 = SimpleBeanPropertyFilter.filterOutAllExcept("name");
        FilterProvider filterProvider5 = new SimpleFilterProvider().addFilter("categoryfilter",filter5);

        MappingJacksonValue mapping5=new MappingJacksonValue(viewChildCategories(categoryid));
        mapping5.setFilters(filterProvider5);

        return mapping5;
    }

    @ApiOperation(value = "fetch filtering details for a category")
    @GetMapping("/customer/category/filter/{categoryId}")
    public FilterCategoryModel filterCategoriesByIdByCustomer(@Valid @PathVariable Long categoryId ){
        return categoryDaoService.filterCategoryByCustomer(categoryId);
    }



}