package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.ImageDocument;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.ImageDocumentRepository;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageDocumentService {

    @Autowired
    ImageDocumentRepository imageDocumentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    Logger logger = LoggerFactory.getLogger(ImageDocumentService.class);


    public String saveImage(ImageDocument imageDocument,Long uid) {

        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            imageDocumentRepository.save(imageDocument);

            user.setProfileImageId(imageDocument.getId());

            userRepository.save(user);

            logger.info("********** Profile Image Uploaded by User **********");

            return "Image Uploaded";

        }
        else
        {
            throw new UserNotFoundException("Invalid User");
        }
    }

    public String saveProductImage(ImageDocument imageDocument, Long pid) {

        Optional<Product> optionalProduct = productRepository.findById(pid);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            imageDocumentRepository.save(imageDocument);

            product.setImageId(imageDocument.getId());
            productRepository.save(product);

            logger.info("********** Product Image Uploaded by Seller **********");

            return "Product Image Uploaded";

        }else
        {
            throw new ResourceNotFoundException("Product Not found");
        }
    }

    public String saveProductVariationImage(ImageDocument imageDocument, Long vid) {

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(vid);

        if (optionalProductVariation.isPresent()) {

            ProductVariation productVariation = optionalProductVariation.get();

            imageDocumentRepository.save(imageDocument);

            productVariation.setImageId(imageDocument.getId());
            productVariationRepository.save(productVariation);

            logger.info("********** Product-Variation Image Uploaded by Seller **********");

            return "Product-Variation Image Uploaded";

        } else {
            throw new ResourceNotFoundException("Invalid Product-Variation Id");
        }
    }


    public ImageDocument downloadUserImage(Long imageid){
        return imageDocumentRepository.findById(imageid)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with imageid " + imageid));
    }


}
