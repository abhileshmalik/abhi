package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.model.ProductVariationUpdateModel;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariantRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

@Service
public class ProductVariationDaoService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ProductVariantService productVariantService;

    Logger logger = LoggerFactory.getLogger(ProductVariationDaoService.class);


    public ProductVariation findVariant(Long variantId) {

        // fetching variant from MysqlDb
        Optional<ProductVariation> variant1 = productVariationRepository.findById(variantId);

        //fetching variant from RedisDb
        Optional<ProductVariant> optionalProductVariant = productVariantRepository.findById(variantId.toString());

        if (variant1.isPresent()) {
            ProductVariation productVariation = variant1.get();

            if (productVariation.getIs_active()) {

                if (optionalProductVariant.isPresent()) {

                    ProductVariant productVariant = optionalProductVariant.get();

                    // checking Quantity from Redis
                    String qty = productVariant.getQuantityAvailable();

                    // update Qty from Redis
                    productVariation.setQuantityAvailable(Integer.parseInt(qty));

                    // saving the Updated values in DB too
                    productVariationRepository.save(productVariation);

                    // returning Product-variation with updated values to user
                    return productVariation;

                }
                else {
                    throw new ResourceNotFoundException("Invalid Variant ID");
                }
            } else {
                 throw new ResourceNotFoundException("Requested Variant is unavailable at the Moment");
                }
        } else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }

    @Transactional
    @Modifying
    public String updateProductVariation(ProductVariationUpdateModel productVariationUpdateModel, Long vid, Long sellerid) {
        Optional<ProductVariation> productVariation = productVariationRepository.findById(vid);

        if (productVariation.isPresent()) {
            ProductVariation savedVariation = productVariation.get();

            Long pid = savedVariation.getProduct().getProduct_id();
            Optional<Product> product = productRepository.findById(pid);

            if (product.isPresent()) {
                Product savedProduct = product.get();
                Long s_id = savedProduct.getSeller().getUser_id();

                if (s_id.equals(sellerid)) {

                    if (productVariationUpdateModel.getVariantName() != null)
                        savedVariation.setVariantName(productVariationUpdateModel.getVariantName());

                    if (productVariationUpdateModel.getQuantityAvailable() != null) {

                        Long variantId = savedVariation.getProduct_variant_id();
                        Integer qty = productVariationUpdateModel.getQuantityAvailable();

                        //updating quantity in RedisDb
                       String message = productVariantService.updateStoredQuantity(variantId, qty);

                    }

                    if (productVariationUpdateModel.getPrice() != null)
                        savedVariation.setPrice(productVariationUpdateModel.getPrice());

                    if (productVariationUpdateModel.getIs_active() != null)
                        savedVariation.setIs_active(productVariationUpdateModel.getIs_active());

                    return "Product Variant Updated Successfully";

                } else {
                    throw new BadRequestException("Product-Variant not associated to current seller");
                }
            }
            else {
                throw new ResourceNotFoundException("Product not found against the selected variant");
            }
        }
        else {
            throw new ResourceNotFoundException("Invalid Variant ID");
        }
    }


    public void autoUpdateVariationQuantity() {

        Iterable<ProductVariation> variations = productVariationRepository.findAll();
        Iterator<ProductVariation> variationsIterator = variations.iterator();

        while (variationsIterator.hasNext()) {
            ProductVariation productVariation = variationsIterator.next();
            Long vid = productVariation.getProduct_variant_id();

            //fetching variant from RedisDb
            Optional<ProductVariant> optionalProductVariant = productVariantRepository.findById(vid.toString());

            if (optionalProductVariant.isPresent()) {

                ProductVariant productVariant = optionalProductVariant.get();

                // checking Quantity from Redis
                String qty = productVariant.getQuantityAvailable();

                // update Qty from Redis
                productVariation.setQuantityAvailable(Integer.parseInt(qty));

                // saving the Updated values in DB too
                productVariationRepository.save(productVariation);

                logger.info("********** Variant Quantity Updated from RedisDb and Stored in MySql Variant **********");

            }
            else {
                logger.error("Unable to find particular Variant from RedisDb for selected MySql Product-Variation ");
            }
        }

    }


}
