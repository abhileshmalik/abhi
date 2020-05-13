package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductVariantRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    Logger logger = LoggerFactory.getLogger(ProductVariantService.class);


    // Not needed as it was manually working too save variant from Mysql to RedisDb
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


    @Transactional
    @Modifying
    public String updateStoredQuantity (Long variantId, Integer qty) {

        Optional<ProductVariant> optionalProductVariant = productVariantRepository.findById(variantId.toString());
        if (optionalProductVariant.isPresent()) {

            ProductVariant productVariant = optionalProductVariant.get();
            productVariant.setQuantityAvailable(qty.toString());

            productVariantRepository.save(productVariant);

            logger.info("********** Updating Quantity of Selected Product-Variant **********");

            return "Quantity updated for selected Variant";

        }
        else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }

    }

    public ProductVariant viewVariant(String vid) {

        Optional<ProductVariant> optionalProductVariant = productVariantRepository.findById(vid);
        if (optionalProductVariant.isPresent()) {

            logger.info("********** Product-Variant Retrieved **********");

            return optionalProductVariant.get();
        }
        else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }
}
