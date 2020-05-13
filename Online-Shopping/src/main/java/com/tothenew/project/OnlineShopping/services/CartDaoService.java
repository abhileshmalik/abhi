package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.orderprocessing.Wishlist;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariant;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CartDaoService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    Logger logger = LoggerFactory.getLogger(CartDaoService.class);


    @Transactional
    @Modifying
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

                                    if (cart.getIs_wishlist_item()) {
                                        wishlistRepository.deleteWishlistItem(customer_user_id, productVariation_id);
                                    }

                                    logger.info("********** Product Added to cart by Customer **********");

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

    public String addtoWishlist (Long customer_id, Long vid) {

        Optional<User> customer = userRepository.findById(customer_id);
        if (customer.isPresent()) {
            User user = new User();
            user = customer.get();

            Customer customer1 = new Customer();
            customer1 = (Customer) user;

            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(vid);

            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = new ProductVariation();
                productVariation = optionalProductVariation.get();

                Long pid = productVariation.getProduct().getProduct_id();

                Optional<Product> optionalProduct = productRepository.findById(pid);
                if (optionalProduct.isPresent()) {

                    Product product = optionalProduct.get();

                    if (!product.getDeleted()) {

                        Wishlist wishlist = new Wishlist();
                        wishlist.setCustomer(customer1);
                        wishlist.setProductVariation(productVariation);
                        wishlistRepository.save(wishlist);

                        logger.info("********** Product Added to Wishlist by Customer **********");

                        return "Item Added to wishlist successfully ";

                    } else {
                        throw new ResourceNotFoundException("Requested Product is no longer available");
                    }

                } else {
                    throw new ResourceNotFoundException("Unable to find Product associated to selected variant");
                }

            } else {
                throw new ResourceNotFoundException("Invalid Product Variation ID");
            }
        } else {
            throw new UserNotFoundException("Invalid customer ID");
        }
    }


}
