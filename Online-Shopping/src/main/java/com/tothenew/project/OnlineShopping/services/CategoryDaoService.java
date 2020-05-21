package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.CategoryMetadataFieldValues;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.model.CategoryModel;
import com.tothenew.project.OnlineShopping.model.FilterCategoryModel;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.product.Product;
import com.tothenew.project.OnlineShopping.product.ProductVariation;
import com.tothenew.project.OnlineShopping.repos.CategoryMetadataFieldValuesRepository;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import com.tothenew.project.OnlineShopping.repos.ProductRepository;
import com.tothenew.project.OnlineShopping.repos.ProductVariationRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.lang.reflect.Type;
import java.util.*;

@Service
public class CategoryDaoService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;


    Logger logger = LoggerFactory.getLogger(CategoryDaoService.class);


    public List<Category> findAll() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        return categories;
    }

    public List<Category> findSubCategories() {
        return categoryRepository.findAllSubCategories();
    }

    public String saveNewCategory(CategoryModel categoryModel) {
        Optional<Category> category= categoryRepository.findByNameIgnoreCase(categoryModel.getName());
        if (category.isPresent())
            return "Category already exists";
        else {
            ModelMapper modelMapper = new ModelMapper();
            Category category1= modelMapper.map(categoryModel, Category.class);

            categoryRepository.save(category1);

            logger.info("********** Added New Category **********");

            return "Category saved";
        }
    }

    public List<Category> viewChildCat(Long categoryid) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryid);
        if (optionalCategory.isPresent())
        {
            Category category = optionalCategory.get();
            Long parentid = category.getSubcategory().getCategory_id();

            logger.info("********** Viewed child categories of same parent **********");

            return categoryRepository.findchildCategoriesOfParent(parentid);

        }
        else throw new ResourceNotFoundException("Invalid Category ID");
    }

    public String saveNewSubCategory(String parentCategory, List<CategoryModel> subCategories){
        Optional<Category> parent_Category=categoryRepository.findByNameIgnoreCase(parentCategory);

        if (parent_Category.isPresent()){
            ModelMapper modelMapper = new ModelMapper();
            Type listType=new TypeToken<List<Category>>(){}.getType();
            List<Category> subCategories1= modelMapper.map(subCategories, listType);

            Category category=new Category();
            category=parent_Category.get();

            //category.addSubCategory(subCategories);

            Category finalCategory = category;
            subCategories1.forEach(e->e.setSubcategory(finalCategory));

            categoryRepository.saveAll(subCategories1);

            logger.info("********** Added New Sub-Categories **********");

            return "Sub-category Added Successfully ";
        }
        else
            throw new ResourceNotFoundException("Parent Category does not exists");
    }

    public String updateCategory(CategoryModel categoryModel, String category){
        Optional<Category> category1=categoryRepository.findByNameIgnoreCase(category);

        if (category1.isPresent()){
            ModelMapper modelMapper = new ModelMapper();
            Category category2= modelMapper.map(categoryModel, Category.class);

            Category category3= category1.get();
            if (category2.getName()!=null)
                category3.setName(category2.getName());
            categoryRepository.save(category3);

            logger.info("********** Category Name Updated **********");

            return "Category updated";
        }
        else
            throw new ResourceNotFoundException("Category name does not exist");
    }


    public FilterCategoryModel filterCategoryByCustomer(Long categoryId) {
        List<Product> productList=productRepository.findSimilar(categoryId, Pageable.unpaged());
        if (productList.isEmpty())
            throw new BadRequestException("There is no product related to this category");
        FilterCategoryModel filterCategoryModel=new FilterCategoryModel();
        List<CategoryMetadataFieldValues> categoryFieldValuesList=new ArrayList<>();
        Iterator<CategoryMetadataFieldValues> categoryFieldValuesIterator= categoryMetadataFieldValuesRepository.findAll().iterator();
        while (categoryFieldValuesIterator.hasNext()) {
            CategoryMetadataFieldValues currentCategoryFieldValues=categoryFieldValuesIterator.next();
            if (currentCategoryFieldValues.getCategory().getCategory_id().equals(categoryId)) {
                categoryFieldValuesList.add(currentCategoryFieldValues);
            }
        }
        Double max=Double.MIN_VALUE;
        Double min=Double.MAX_VALUE;
        Set<String> brandsList=new HashSet<>();
        Iterator<Product> productIterator= productRepository.findSimilar(categoryId,Pageable.unpaged()).iterator();
        while (productIterator.hasNext()) {
            Product currentProduct = productIterator.next();
            if (currentProduct.getCategory().getCategory_id().equals(categoryId)) {
                brandsList.add(currentProduct.getBrand());
                Iterator<ProductVariation> productVariantIterator = productVariationRepository.findByProductId(currentProduct.getProduct_id()).iterator();
                while (productVariantIterator.hasNext()) {
                    ProductVariation currentVariant = productVariantIterator.next();
                    if (currentVariant.getPrice() <= min)
                        min = currentVariant.getPrice();
                    if (currentVariant.getPrice() >= max)
                        max = currentVariant.getPrice();
                }
            }
        }
        filterCategoryModel.setCategoryFieldValues(categoryFieldValuesList);
        if (max>0)
            filterCategoryModel.setMaxPrice(max);
        if(min<Integer.MAX_VALUE)
            filterCategoryModel.setMinPrice(min);
        filterCategoryModel.setBrandsList(brandsList);

        return filterCategoryModel;
    }

}