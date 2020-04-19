package com.tothenew.project.OnlineShopping.services;


import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.model.ProductVariationUpdateModel;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductVariationDaoService {

    @Autowired
    private ProductRepository productRepository;

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

    @Transactional
    @Modifying
    public String updateProductVariation(ProductVariationUpdateModel productVariationUpdateModel, Long vid) {
        Optional<ProductVariation> productVariation = productVariationRepository.findById(vid);

        if (productVariation.isPresent()) {
            ProductVariation savedVariation = productVariation.get();

            /*Long pid = savedVariation.getProduct_variant_id();
            Optional<Product> product = productRepository.findById(pid);

            if (product.isPresent()) {
                Product savedProduct = product.get();

                Long s_id = savedProduct.getSeller().getUser_id();*/

                //if(s_id.equals(sellerid)) {

                    if (productVariationUpdateModel.getVariantName() != null)
                        savedVariation.setVariantName(productVariationUpdateModel.getVariantName());

                    if (productVariationUpdateModel.getQuantityAvailable() != null)
                        savedVariation.setQuantityAvailable(productVariationUpdateModel.getQuantityAvailable());

                    if (productVariationUpdateModel.getPrice() != null)
                        savedVariation.setPrice(productVariationUpdateModel.getPrice());

                   if (productVariationUpdateModel.getIs_active() != null)
                        savedVariation.setIs_active(productVariationUpdateModel.getIs_active());


                return "Product Variant Updated Successfully";

            }
          /*  else {
                throw new BadRequestException("Product not associated to current seller");
            }*/

        else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }


}
