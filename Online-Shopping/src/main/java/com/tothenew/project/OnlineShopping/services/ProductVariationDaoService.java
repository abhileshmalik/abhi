package com.tothenew.project.OnlineShopping.services;


import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductVariationDaoService {

    @Autowired
    private ProductVariationRepository productVariationRepository;


    public ProductVariation findVariant(Long variantId) {
        Optional<ProductVariation> variant1 = productVariationRepository.findById(variantId);

        if (variant1.isPresent()) {
            ProductVariation productVariation = variant1.get();
            return productVariation;
        } else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }

}
