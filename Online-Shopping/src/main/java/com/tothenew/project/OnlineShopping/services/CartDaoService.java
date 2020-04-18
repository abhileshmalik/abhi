package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.CartRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
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
    private ProductVariationRepository productVariationRepository;


    public String addToCart( Cart cart, Long customer_user_id, Long productVariation_id) {

        Optional<User> customer = userRepository.findById(customer_user_id);
        if (customer.isPresent()) {
            User user = new User();
            user = customer.get();

            Customer customer1 = new Customer();
            customer1 = (Customer) user;

            cart.setCustomer(customer1);

            Optional<ProductVariation> productVariation = productVariationRepository.findById(productVariation_id);
            if (productVariation.isPresent()) {
                ProductVariation productVariation1 = new ProductVariation();
                productVariation1 = productVariation.get();
                if (productVariation1.getIs_active()) {
                    Integer qty = cart.getQuantity();
                    if (qty < productVariation1.getQuantityAvailable()) {
                        cart.setProductVariation(productVariation1);
                        cartRepository.save(cart);
                        return "Item Added to cart Successfully ";
                    } else {
                        throw new ResourceNotFoundException("Ordered Quantity is greater than available stock in Warehouse.");
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
