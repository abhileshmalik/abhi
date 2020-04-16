package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.dto.ProductReviewDto;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductReview;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductReviewRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public String addReview(ProductReviewDto productReviewDto, Long customer_user_id, Long product_id){

        Optional<User> customer = userRepository.findById(customer_user_id);
        Optional<Product> product= productRepository.findById(product_id);

        if(!customer.isPresent())
            throw new UserNotFoundException("User not found");

        else if(!product.isPresent())
            throw new ResourceNotFoundException("Product not found");

        else {
            User user=new User();
            user=customer.get();

            Customer customer1=new Customer();
            customer1=(Customer)user;

            ModelMapper modelMapper = new ModelMapper();
            ProductReview productReview= modelMapper.map(productReviewDto, ProductReview.class);

            productReview.setCustomer(customer1);

            Product product1= new Product();
            product1= product.get();

            productReview.setProduct(product1);

            productReviewRepository.save(productReview);

            return "Review posted successfully ....... ";
        }
    }
}
