package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDaoService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Category category;


    public List<Product> findAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products;
    }


/*    public Product addNewProduct(Product product) {
        productRepository.save(product);
        return product;
    }*/


    public String addNewProduct(Long seller_user_id,List<Product> products, String category_name){

        Optional<User> seller=userRepository.findById(seller_user_id);
        if (seller.isPresent()) {
            User seller1 = new User();            //Object of user created as user id is in User Entity
            seller1 = seller.get();

            Seller seller2 = new Seller();
            seller2 = (Seller) seller1;            // parsing user to Seller


            //products.forEach(e->e.setSeller(finalSeller));

            Seller finalSeller = seller2;
            products.forEach(e -> e.setSeller(finalSeller));

            Optional<Category> category1 = categoryRepository.findByName(category_name);
            if (category1.isPresent()) {
                category = category1.get();       // get is function of Optional

                //category.setProducts(products);

                products.forEach(e -> e.setCategory(category));
                products.forEach(e-> e.setIsActive(true));
            }
            else
            {
                throw new ResourceNotFoundException("Invalid Category name");
            }


            productRepository.saveAll(products);
            return " Products Added Successfully ";
        }
        else
            throw new UserNotFoundException("Invalid Seller ID");
    }

    public List<Product> findCategoryProducts(String category_name) {
        String category=categoryRepository.findByCatName(category_name);
        return productRepository.findAllProducts(category);
    }

    public Product viewparticularProduct(String product_name) {
        Product p1 = productRepository.findByProductName(product_name);
        return p1;
    }

    public Product findProduct(Long pid) {
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()) {
            Product p1 = product.get();
            return p1;
        }
        else
            throw new ResourceNotFoundException("Invalid Product ID");
    }
}
