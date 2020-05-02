package com.tothenew.project.OnlineShopping.model;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductVariationModel {

    @NotNull
    private String variantName;
    @NotNull
    private Integer quantityAvailable;
    @NotNull
    private Double price;

    @NotNull
    private Map<String, String> attributes = new LinkedHashMap<>();


    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
