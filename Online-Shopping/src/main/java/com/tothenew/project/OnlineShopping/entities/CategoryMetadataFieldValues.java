package com.tothenew.project.OnlineShopping.entities;

import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.utils.CategoryMetadataFieldValuesID;

import javax.persistence.*;

@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMetadataFieldValuesID id= new CategoryMetadataFieldValuesID();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    private String value;

    public CategoryMetadataFieldValuesID getId() {
        return id;
    }

    public void setId(CategoryMetadataFieldValuesID id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }
}
