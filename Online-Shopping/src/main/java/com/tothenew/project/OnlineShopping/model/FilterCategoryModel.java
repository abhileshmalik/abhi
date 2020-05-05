package com.tothenew.project.OnlineShopping.model;

import com.tothenew.project.OnlineShopping.entities.CategoryMetadataFieldValues;

import javax.persistence.ElementCollection;
import java.util.List;
import java.util.Set;

public class FilterCategoryModel {

    @ElementCollection
    private List<CategoryMetadataFieldValues> categoryFieldValues;
    @ElementCollection
    private Set<String> brandsList;
    private Double maxPrice=0.0;
    private Double minPrice=0.0;

    public List<CategoryMetadataFieldValues> getCategoryFieldValues() {
        return categoryFieldValues;
    }

    public void setCategoryFieldValues(List<CategoryMetadataFieldValues> categoryFieldValues) {
        this.categoryFieldValues = categoryFieldValues;
    }

    public Set<String> getBrandsList() {
        return brandsList;
    }

    public void setBrandsList(Set<String> brandsList) {
        this.brandsList = brandsList;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
}
