package com.tothenew.project.OnlineShopping.orderprocessing;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@ApiModel(description = "Details about the Customer's Wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_user_id")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }

}
