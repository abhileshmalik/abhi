package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.orderprocessing.Cart;
import com.tothenew.project.OnlineShopping.orderprocessing.OrderProduct;
import com.tothenew.project.OnlineShopping.orderprocessing.Orders;
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

    public Orders addToOrder(Long customer_user_id, Orders orders, Long cart_id){

        Optional<User> customer = userRepository.findById(customer_user_id);
        if(customer.isPresent()) {
            User user = new User();
            user = customer.get();

            Customer customer1 = new Customer();
            customer1 = (Customer) user;

            orders.setCustomer(customer1);

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

            }
            else {
                throw new ResourceNotFoundException("Address not found, Check Address Label");
            }

            Optional<Cart> cartId = cartRepository.findById(cart_id);

            if (cartId.isPresent()) {
                Cart cart = new Cart();
                cart = cartId.get();

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(orders);
                orderProduct.setProductVariation(cart.getProductVariation());
                orderProduct.setQuantity(cart.getQuantity());

                ProductVariation product_variation = cart.getProductVariation();

                orderProduct.setPrice(product_variation.getPrice());

                Double amount = orderProduct.getPrice() * cart.getQuantity();
                orders.setAmountPaid(amount);

                Integer originalqty = product_variation.getQuantityAvailable();
                Integer reducedqty = cart.getQuantity();


                product_variation.setQuantityAvailable(originalqty-reducedqty);

                productVariationRepository.save(product_variation);
                orderProductRepository.save(orderProduct);
                orderRepository.save(orders);

                return orders;
            } else {
                throw new ResourceNotFoundException("Invalid Cart ID");
            }
        }
        else
        {
            throw new UserNotFoundException("Invalid Customer ID");
        }
    }
}
