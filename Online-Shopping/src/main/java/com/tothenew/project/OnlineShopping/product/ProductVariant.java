package com.tothenew.project.OnlineShopping.product;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash
public class ProductVariant {

    @Id
    private Long id;
    private String variantName;
    private Integer quantityAvailable;
    private Double price;
    private Boolean is_active;
}
