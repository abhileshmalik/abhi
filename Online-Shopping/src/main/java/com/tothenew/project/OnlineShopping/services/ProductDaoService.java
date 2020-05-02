package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.model.ProductUpdateModel;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private EmailSenderService emailSenderService;

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
                products.forEach(e-> e.setIsActive(false));
                products.forEach(e-> e.setDeleted(false));

                productRepository.saveAll(products);
                return " Products Added Successfully ";

            }
            else
            {
                throw new ResourceNotFoundException("Invalid Category name");
            }
        }
        else
            throw new UserNotFoundException("Invalid Seller ID");
    }

    public List<Product> findCategoryProducts(String category_name) {
        String category=categoryRepository.findByCatName(category_name);
        return productRepository.findAllProducts(category);
    }

    // Find Product by name....
    public Product viewparticularProduct(String product_name) {
        Product p1 = productRepository.findByProductName(product_name);
        return p1;
    }

    // Find Product by id....
    public Product findProduct(Long pid) {
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()) {
            Product p1 = product.get();
            return p1;
        }
        else
            throw new ResourceNotFoundException("Invalid Product ID");
    }

    @Transactional
    public String activateProduct(Long pid) {

        Optional<Product> product = productRepository.findById(pid);
        if (product.isPresent()) {
            Product product1 = product.get();
            Seller seller = product1.getSeller();

            String emailid = seller.getEmail();

            if(!product1.getIsActive())
            {
                product1.setIsActive(true);
                productRepository.save(product1);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(emailid);
                mailMessage.setSubject("Product Activated!!");
                mailMessage.setFrom("wishcart@gmail.com");
                mailMessage.setText("Your product has been Activated by our Team" +
                        " Customers can now view it and place orders for same.");
                emailSenderService.sendEmail((mailMessage));
                return "Product Activated";
            }
            else
            {
                return "Product is already Activated";
            }

        } else {
            throw new ResourceNotFoundException("Incorrect Product ID");
        }
    }

    @Transactional
    public String deactivateProduct(Long pid)
    {
        Optional<Product> product = productRepository.findById(pid);
        if (product.isPresent()) {
            Product product1 = product.get();
            Seller seller = product1.getSeller();

            String emailid = seller.getEmail();

            if(product1.getIsActive())
            {
                product1.setIsActive(false);
                productRepository.save(product1);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(emailid);
                mailMessage.setSubject("Product Deactivated!!");
                mailMessage.setFrom("wishcart@gmail.com");
                mailMessage.setText("Your product has been deactivated by our Team" +
                        "Please contact our team for assistance");
                emailSenderService.sendEmail((mailMessage));
                return "Product Deactivated";
            }
            else
            {
                return "Product is already deactivated";
            }

        } else {
            throw new ResourceNotFoundException("Incorrect Product ID");
        }
    }

    @Transactional
    @Modifying
    public String updateProduct(ProductUpdateModel productUpdateModel, Long pid, Long sellerid) {
        Optional<Product> product = productRepository.findById(pid);

        if (product.isPresent()) {
            Product savedProduct = product.get();

            Long s_id = savedProduct.getSeller().getUser_id();

            if(s_id.equals(sellerid)) {
                if (productUpdateModel.getProductName() != null)
                    savedProduct.setProductName(productUpdateModel.getProductName());

                if (productUpdateModel.getBrand() != null)
                    savedProduct.setBrand(productUpdateModel.getBrand());

                if (productUpdateModel.getProductDescription() != null)
                    savedProduct.setProductDescription(productUpdateModel.getProductDescription());

                if (productUpdateModel.getCancellable() != null)
                    savedProduct.setIsCancellable(productUpdateModel.getCancellable());

                if (productUpdateModel.getReturnable() != null)
                    savedProduct.setIsReturnable(productUpdateModel.getReturnable());

                return "Product Updated Successfully";

            }
            else {
                throw new BadRequestException("Product not associated to current seller");
            }

        }
        else {
            throw new ResourceNotFoundException("Invalid Product ID");
        }
    }


    public String deleteProduct(Long pid, Long sellerid) {
        Optional<Product> product = productRepository.findById(pid);

        if (product.isPresent()) {
            Product savedProduct = product.get();
            Long s_id = savedProduct.getSeller().getUser_id();

            if (s_id.equals(sellerid)) {

                savedProduct.setDeleted(true);
            productRepository.save(savedProduct);
            return "Product Deleted Successfully";

            }
            else {
                throw new BadRequestException("Product not associated to current seller");
            }

        }
        else {
            throw new ResourceNotFoundException("Invalid Product ID");
        }
    }

    public List<Product> findSellerProducts(Long sellerid) {
        return productRepository.findSellerAssociatedProducts(sellerid);

    }
}
