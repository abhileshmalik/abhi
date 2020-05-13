package com.tothenew.project.OnlineShopping.product;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tothenew.project.OnlineShopping.utils.HashMapConverter;
import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@JsonFilter("variantfilter")
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "All details about the Product-Variation")
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long product_variant_id;
    private String variantName;
    private Integer quantityAvailable;
    private Double price;
    private Boolean is_active;
    private Long imageId;

    @Column
    @CreatedDate
    private Date createdDate;

    @Column
    @LastModifiedDate
    private Date modifiedDate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;


    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> productAttributes;

    public Long getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(Long product_variant_id) {
        this.product_variant_id = product_variant_id;
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

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Map<String, Object> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(Map<String, Object> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
