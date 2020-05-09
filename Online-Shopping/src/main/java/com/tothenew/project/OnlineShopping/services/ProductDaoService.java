package com.tothenew.project.OnlineShopping.services;

import com.google.common.collect.Sets;
import com.tothenew.project.OnlineShopping.entities.CategoryMetadataField;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.model.ProductUpdateModel;
import com.tothenew.project.OnlineShopping.model.ProductVariationModel;
import com.tothenew.project.OnlineShopping.model.ProductViewModel;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.*;
import com.tothenew.project.OnlineShopping.utils.StringToSetParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductDaoService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

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


    public String saveNewProductVariation(ProductVariationModel productVariationModel, Long product_id, Seller seller) {

        Long sellerid = seller.getUser_id();
        Optional<Product> optionalProduct= productRepository.findById(product_id);

        if (optionalProduct.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            ProductVariation productVariation= modelMapper.map(productVariationModel, ProductVariation.class);

            Product product = optionalProduct.get();
            Long sid = product.getSeller().getUser_id();

            if (sid.equals(sellerid)) {

                String message= validateNewProductVariation(productVariationModel, product);

                if (message.equals("Success")){

                    if(productVariation.getQuantityAvailable()<=0){
                        return "Quantity should be greater than 0.";
                    }
                    else if(productVariation.getPrice()<=0){
                        return "Price should be greater than 0";
                    }
                    else {
                        productVariation.setProduct(product);
                        productVariation.setIs_active(true);
                        productVariationRepository.save(productVariation);
                        return "Product Variant saved";
                    }
                }
                return message;
            }
            else
            {
                throw new BadRequestException("Product not associated to logged in seller");
            }
        }else{
            throw new ResourceNotFoundException("Product does not exists");
        }
    }

    public String validateNewProductVariation(ProductVariationModel productVariationModel, Product product){

        // check if all the fields are actually related to the product category.
        Category category = product.getCategory();
        Map<String, String> attributes = productVariationModel.getAttributes();

        List<String> receivedFields = new ArrayList<>(attributes.keySet());
        List<String> actualFields = new ArrayList<>();
        categoryMetadataFieldValuesRepository.findAllFieldsOfCategoryById(category.getCategory_id())
                .forEach((e)->{
                    actualFields.add(e[0].toString());
                });

        if(receivedFields.size() < actualFields.size()){
            return "Please provide all the fields related to the product category.";
        }

        receivedFields.removeAll(actualFields);
        if(receivedFields.isEmpty()){
            return "Invalid fields found in the data.";
        }

        // check validity of values of fields.
        List<String> receivedFieldsCopy = new ArrayList<>(attributes.keySet());

        for (String receivedField : receivedFieldsCopy) {

            CategoryMetadataField field = categoryMetadataFieldRepository.findByName(receivedField);

            List<Object> savedValues = categoryMetadataFieldValuesRepository.findAllValuesOfCategoryField(category.getCategory_id(),field.getId());

            String values = savedValues.get(0).toString();
            Set<String> actualValueSet = StringToSetParser.toSetOfValues(values);

            String receivedValues = attributes.get(receivedField);
            Set<String> receivedValueSet = StringToSetParser.toSetOfValues(receivedValues);

            if(!Sets.difference(receivedValueSet, actualValueSet).isEmpty()){
                return "Invalid value found for field "+receivedField;
            }
        }
        return "Success";
    }


    public List<Product> findCategoryProducts(String category_name, String page, String size) {
        String category=categoryRepository.findByCatName(category_name);
        return productRepository.findAllProducts(category, PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
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
            if (p1.getIsActive() && !p1.getDeleted()) {
                return p1;
            }
            else {
                throw new ResourceNotFoundException("Product is unavailable at the moment");
            }
        }
        else
            throw new ResourceNotFoundException("Invalid Product ID");
    }

/*    // Find Product by id....  (Using Product view model)
    public ProductViewModel findProduct(Long pid) {
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()) {
            Product p1 = product.get();
            if (p1.getIsActive() && !p1.getDeleted()) {
                // return p1;

                ProductViewModel productViewModel = new ProductViewModel();
                BeanUtils.copyProperties(p1,productViewModel);
                productViewModel.setCategory(p1.getCategory().getName());
                productViewModel.setCancellable(p1.getIsCancellable());
                productViewModel.setReturnable(p1.getIsReturnable());

                return productViewModel;
            }
            else {
                throw new ResourceNotFoundException("Product is unavailable at the moment");
            }
        }
        else
            throw new ResourceNotFoundException("Invalid Product ID");
    }*/

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
            Iterable<ProductVariation> variations = productVariationRepository.findByProductId(savedProduct.getProduct_id());
            Iterator<ProductVariation> variationIterator = variations.iterator();

            if (s_id.equals(sellerid)) {

                while(variationIterator.hasNext()){
                    ProductVariation productVariation = variationIterator.next();
                    productVariation.setIs_active(true);
                    productVariationRepository.save(productVariation);
                }
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

    public List<Product> findSimilarProducts(Long pid, String page, String size) {
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()) {
            Product product1 = product.get();
            Long categoryId = product1.getCategory().getCategory_id();
            return productRepository.findSimilar(categoryId, PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
        }
        else
            throw new ResourceNotFoundException("Invalid Product ID");

    }
}
