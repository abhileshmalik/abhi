package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.orderprocessing.OrderProduct;
import com.tothenew.project.OnlineShopping.orderprocessing.Orders;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderDaoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;


    public String addToOrder(Orders orders,Long customer_user_id, Long cart_id) {

        Optional<Cart> cartId = cartRepository.findById(cart_id);

        if (cartId.isPresent()) {
            Cart cart = new Cart();
            cart = cartId.get();

            Customer customer = cart.getCustomer();

            if (customer.getUser_id().equals(customer_user_id)) {
                orders.setCustomer(customer);

                Address address = new Address();
                String address_label = orders.getCustomerAddressLabel();
                Optional<Address> address1 = addressRepository.findByAdd(address_label, customer_user_id);
                if (address1.isPresent()) {
                    address = address1.get();

                    orders.setCustomerAddressAddressLine(address.getAddressLine());
                    orders.setCustomerAddressCity(address.getCity());
                    orders.setCustomerAddressState(address.getState());
                    orders.setCustomerAddressCountry(address.getCountry());
                    orders.setCustomerAddressZipCode(address.getZipCode());
                    orders.setDateCreated(new Date());

                } else {
                    throw new ResourceNotFoundException("Address not found, Check Address Label");
                }
            } else {
                throw new ResourceNotFoundException("Cart not associated to current customer");
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(orders);
            orderProduct.setProductVariation(cart.getProductVariation());
            orderProduct.setQuantity(cart.getQuantity());

            ProductVariation product_variation = cart.getProductVariation();
            if (product_variation.getIs_active()) {

                Long pid = product_variation.getProduct().getProduct_id();

                Optional<Product> optionalProduct = productRepository.findById(pid);
                if (optionalProduct.isPresent()) {

                    Product product = optionalProduct.get();

                    if (!product.getDeleted() && product.getIsActive()) {

                        orderProduct.setPrice(product_variation.getPrice());
                        Double amount = orderProduct.getPrice() * cart.getQuantity();
                        orders.setAmountPaid(amount);

                        Integer originalqty = product_variation.getQuantityAvailable();
                        Integer orderedqty = cart.getQuantity();

                        if (originalqty > orderedqty) {
                            product_variation.setQuantityAvailable(originalqty - orderedqty);

                            productVariationRepository.save(product_variation);
                            orderProductRepository.save(orderProduct);
                            orderRepository.save(orders);

                            return "Order Placed Successfully.... " +
                                    "Thank You for Choosing Online-Shopping portal";
                        } else {
                            throw new ResourceNotFoundException("Sorry the Requested quantity is not yet available in warehouse.");
                        }
                    } else {
                        throw new ResourceNotFoundException("Sorry, The Requested product is unavailable at the moment.");
                    }
                } else {
                    throw new ResourceNotFoundException("Unable to find Product associated to selected variant");
                }
            }
            else {
                throw new ResourceNotFoundException("Requested variant is unavailable at the moment");
            }
        } else {
            throw new ResourceNotFoundException("Invalid Cart ID");
        }
    }
}
