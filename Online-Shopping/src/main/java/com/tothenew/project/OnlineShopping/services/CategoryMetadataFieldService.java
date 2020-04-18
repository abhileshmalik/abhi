package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.CategoryMetadataField;
import com.tothenew.project.OnlineShopping.entities.CategoryMetadataFieldValues;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.model.CategoryMetadataFieldModel;
import com.tothenew.project.OnlineShopping.model.MetadataFieldValueInsertModel;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.repos.CategoryMetadataFieldRepository;
import com.tothenew.project.OnlineShopping.repos.CategoryMetadataFieldValuesRepository;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import com.tothenew.project.OnlineShopping.utils.StringToMapParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMetadataFieldService {

    @Autowired
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public String addNewMetadataField(String fieldName){
        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findByName(fieldName);
        if (categoryMetadataField!=null){
            throw new BadRequestException("Metadata field already exists");
        }
        else{
            CategoryMetadataField categoryMetadataField1= new CategoryMetadataField();
            categoryMetadataField1.setName(fieldName);
            categoryMetadataFieldRepository.save(categoryMetadataField1);
            return "Category metadata field created";
        }
    }

    public String addNewMetadataFieldValues(MetadataFieldValueInsertModel fieldValueDtos, Long categoryId, Long metaFieldId){

        Optional<Category> category= categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> categoryMetadataField= categoryMetadataFieldRepository.findById(metaFieldId);
        if (!category.isPresent())
            throw new ResourceNotFoundException("Category does not exists");
        else if (!categoryMetadataField.isPresent())
            throw new ResourceNotFoundException("Metadata field does not exists");
        else{
            Category category1= new Category();
            category1= category.get();

            CategoryMetadataField categoryMetadataField1= new CategoryMetadataField();
            categoryMetadataField1= categoryMetadataField.get();

            CategoryMetadataFieldValues categoryFieldValues = new CategoryMetadataFieldValues();

            for(CategoryMetadataFieldModel fieldValuePair : fieldValueDtos.getFieldValues()){

                String values = StringToMapParser.toCommaSeparatedString(fieldValuePair.getValues());

                categoryFieldValues.setValue(values);
                categoryFieldValues.setCategory(category1);
                categoryFieldValues.setCategoryMetadataField(categoryMetadataField1);

                categoryMetadataFieldValuesRepository.save(categoryFieldValues);
            }
            return "Metadata field values added successfully";
        }

    }

}
