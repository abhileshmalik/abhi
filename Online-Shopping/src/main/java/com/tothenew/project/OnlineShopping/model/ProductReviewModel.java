package com.tothenew.project.OnlineShopping.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class ProductReviewModel {

    private String review;

    @NotNull
    @Range(min = 0, max = 10)
    private Integer rating;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
