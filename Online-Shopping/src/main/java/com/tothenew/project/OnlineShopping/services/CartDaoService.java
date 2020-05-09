package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartDaoService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;


    public String addToCart( Cart cart, Long customer_user_id, Long productVariation_id) {

        Optional<User> customer = userRepository.findById(customer_user_id);
        if (customer.isPresent()) {
            User user = new User();
            user = customer.get();

            Customer customer1 = new Customer();
            customer1 = (Customer) user;

            cart.setCustomer(customer1);

            // variant from Mysql
            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVariation_id);

            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = new ProductVariation();
                productVariation = optionalProductVariation.get();

                if (productVariation.getIs_active()) {

                    Long pid = productVariation.getProduct().getProduct_id();

                    // Finding the Variant from RedisDb
                    Optional<ProductVariant> optionalProductVariant = productVariantRepository.findById(productVariation_id.toString());

                    Optional<Product> optionalProduct = productRepository.findById(pid);
                    if (optionalProduct.isPresent()) {

                        Product product = optionalProduct.get();

                        if(!product.getDeleted() && product.getIsActive()) {

                            if (optionalProductVariant.isPresent()) {

                                ProductVariant productVariant = optionalProductVariant.get();

                                // fetching quantity of variant from Redis instead of mysql;
                                String RedisVariantQty = productVariant.getQuantityAvailable();

                                Integer originalqty = Integer.parseInt(RedisVariantQty);

                                Integer cartQuantity = cart.getQuantity();
                                if (cartQuantity < originalqty) {
                                    cart.setProductVariation(productVariation);
                                    cartRepository.save(cart);

                                    return "Item Added to cart Successfully ";

                                } else {
                                    throw new ResourceNotFoundException("Ordered Quantity is greater than available stock in Warehouse.");
                                }
                            }  else {
                                    throw new ResourceNotFoundException("Invalid Variant ID");
                            }
                    }
                        else {
                            throw new ResourceNotFoundException("Sorry, The Requested product is unavailable at the moment.");
                        }
                    }
                    else {
                        throw new ResourceNotFoundException("Unable to find Product associated to selected variant");
                    }
                }
                else
                {
                    throw new ResourceNotFoundException("Requested variant is unavailable at the moment");
                }
            }
            else {
                throw new ResourceNotFoundException("Invalid Product Variation ID");
            }
        }
        else {
            throw new UserNotFoundException("Invalid customer ID");
        }
    }

}
