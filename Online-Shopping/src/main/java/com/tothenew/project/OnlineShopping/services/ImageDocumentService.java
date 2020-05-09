package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.ImageDocument;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.repos.ImageDocumentRepository;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
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

    public String saveImage(ImageDocument imageDocument,Long uid) {

        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            imageDocumentRepository.save(imageDocument);

            user.setProfileImageId(imageDocument.getId());

            userRepository.save(user);

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

            return "Product Image Uploaded";

        }else
        {
            throw new ResourceNotFoundException("Product Not found");
        }
    }


}
