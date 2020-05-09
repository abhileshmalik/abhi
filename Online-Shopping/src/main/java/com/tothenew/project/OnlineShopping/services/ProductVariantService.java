package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductVariantRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;


    public String saveVariant(Long id) {

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(id);
        if (optionalProductVariation.isPresent())
        {
            ProductVariation productVariation = optionalProductVariation.get();

            ProductVariant productVariant = new ProductVariant();

            Long vid = productVariation.getProduct_variant_id();
            Integer qty = productVariation.getQuantityAvailable();

            productVariant.setVid(vid.toString());
            productVariant.setQuantityAvailable(qty.toString());

            productVariantRepository.save(productVariant);

            return "Variant Saved Successfully";


        }
        else {
            throw new ResourceNotFoundException("Invalid Product-Variation id");
        }
    }


    public ProductVariant viewVariant(String vid) {

        Optional<ProductVariant> optionalProductVariantVariant = productVariantRepository.findById(vid);
        if (optionalProductVariantVariant.isPresent()) {
            return optionalProductVariantVariant.get();
        }
        else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }
}
