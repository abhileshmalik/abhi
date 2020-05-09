package com.tothenew.project.OnlineShopping.product;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("product_variant")
@ApiModel(description = "Product-Variant information stored in RedisDb for frequent access")
public class ProductVariant {

    @Id
    private String vid;
    private String quantityAvailable;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

}
