package com.tothenew.project.OnlineShopping.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.model.ProductUpdateModel;
import com.tothenew.project.OnlineShopping.model.ProductVariationModel;
import com.tothenew.project.OnlineShopping.model.ProductVariationUpdateModel;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.services.ProductVariationDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(value="Product Related APIs")
@RestController
public class ProductController {

    @Autowired
    private ProductDaoService productDaoService;

    @Autowired
    private ProductVariationDaoService productVariationDaoService;

    @Autowired
    private UserDaoService userDaoService;


    public List<Product> retrieveAllProducts () {
        return productDaoService.findAll();
    }

    public List<Product> findCategorywiseProducts(String category_name, String page, String size){
        return productDaoService.findCategoryProducts(category_name, page, size);
    }

    @ApiOperation(value = "List All Products based on selected category")
    @GetMapping("/products/{category_name}")
    public MappingJacksonValue retrieveProductList(@PathVariable String category_name,
                                                   @RequestHeader(defaultValue = "0") String page,
                                                   @RequestHeader(defaultValue = "10")String size)
    {
        SimpleBeanPropertyFilter filter6 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider6 = new SimpleFilterProvider().addFilter("productfilter",filter6);

        MappingJacksonValue mapping6 = new MappingJacksonValue(findCategorywiseProducts(category_name, page, size));

        mapping6.setFilters(filterProvider6);

        return mapping6;
    }

    @ApiOperation(value = "Add new Product")
    @PostMapping("/save-product/category/{category_name}")
    public ResponseEntity<Object> saveProduct(@Valid @RequestBody List<Product> products,
                                              @PathVariable String category_name){

        Seller seller = userDaoService.getLoggedInSeller();
        Long seller_user_id = seller.getUser_id();
        String message = productDaoService.addNewProduct(seller_user_id, products, category_name);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/save-productVariation/{product_id}")
    public String saveProductVariation(@Valid @RequestBody ProductVariationModel productVariationModel,
                                       @PathVariable Long product_id){

        Seller seller = userDaoService.getLoggedInSeller();

        return productDaoService.saveNewProductVariation(productVariationModel, product_id, seller);
    }

    @ApiOperation(value = "Get a Product by Id")
    @GetMapping("/product/{product_id}")
    public MappingJacksonValue retrieveProduct(@PathVariable Long product_id) {
        SimpleBeanPropertyFilter filter7 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider7 = new SimpleFilterProvider().addFilter("productfilter",filter7);

        MappingJacksonValue mapping7=new MappingJacksonValue(viewProduct(product_id));
        mapping7.setFilters(filterProvider7);

        return mapping7;
    }

    public Product viewProduct(Long product_id) {
        return productDaoService.findProduct(product_id);
    }


    @ApiOperation(value = "Update Product by Id")
    @PutMapping("/seller/updateproduct/{pid}")
    public ResponseEntity<Object> updateProductDetails(@RequestBody ProductUpdateModel productUpdateModel, @PathVariable Long pid){
        Seller seller = userDaoService.getLoggedInSeller();
        Long sellerid = seller.getUser_id();
        String message = productDaoService.updateProduct(productUpdateModel, pid, sellerid);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Product-Variant by Id")
    @PutMapping("/seller/updateproduct/variant/{vid}")
    public ResponseEntity<Object> updateProductVariant(@RequestBody ProductVariationUpdateModel productVariationUpdateModel, @PathVariable Long vid){
      //  Seller seller = userDaoService.getLoggedInSeller();
      //  Long sellerid = seller.getUser_id();
        String message = productVariationDaoService.updateProductVariation(productVariationUpdateModel, vid);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete Product by Id")
    @DeleteMapping("/seller/deleteproduct/{pid}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long pid) {

        Seller seller = userDaoService.getLoggedInSeller();
        Long sellerid = seller.getUser_id();

        String message = productDaoService.deleteProduct(pid, sellerid);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public List<Product> findSimilarproducts(Long pid, String page, String size) {
        return productDaoService.findSimilarProducts(pid, page, size);
    }

    @ApiOperation(value = "List all products having same category as given product id")
    @GetMapping("/similar-products/products/{pid}")
    public MappingJacksonValue retrieveSimilarProducts(@PathVariable Long pid,
                                                       @RequestHeader(defaultValue = "0") String page,
                                                       @RequestHeader(defaultValue = "10") String size) {

        SimpleBeanPropertyFilter filter10 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider10 = new SimpleFilterProvider().addFilter("productfilter",filter10);

        MappingJacksonValue mapping10 = new MappingJacksonValue(findSimilarproducts(pid, page, size));

        mapping10.setFilters(filterProvider10);

        return mapping10;
    }


    public List<Product> findSellerwiseProducts(Long sellerid){
        return productDaoService.findSellerProducts(sellerid);
    }


    @ApiOperation(value = "Enlist all products added by Seller")
    @GetMapping("/seller/products")
    public MappingJacksonValue retrieveSellerProducts() {
        Seller seller = userDaoService.getLoggedInSeller();
        Long sellerid = seller.getUser_id();

        SimpleBeanPropertyFilter filter10 = SimpleBeanPropertyFilter.filterOutAllExcept("productName","brand",
                "productDescription","isCancellable","isReturnable","variations");

        FilterProvider filterProvider10 = new SimpleFilterProvider().addFilter("productfilter",filter10);

        MappingJacksonValue mapping10 = new MappingJacksonValue(findSellerwiseProducts(sellerid));

        mapping10.setFilters(filterProvider10);

        return mapping10;
    }






}
