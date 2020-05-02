package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.model.CategoryModel;
import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryDaoService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        return categories;
    }

    public List<Category> findSubCategories() {
        return categoryRepository.findAllSubCategories();
    }

    public String saveNewCategory(CategoryModel categoryModel) {
        Optional<Category> category= categoryRepository.findByName(categoryModel.getName());
        if (category.isPresent())
            return "Category already exists";
        else {
            ModelMapper modelMapper = new ModelMapper();
            Category category1= modelMapper.map(categoryModel, Category.class);

            categoryRepository.save(category1);
            return "Category saved";
        }
    }

    public List<Category> viewChildCat(Long categoryid) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryid);
        if (optionalCategory.isPresent())
        {
            Category category = optionalCategory.get();
            Long parentid = category.getSubcategory().getCategory_id();
            return categoryRepository.findchildCategoriesOfParent(parentid);

        }
        else throw new ResourceNotFoundException("Invalid Category ID");
    }

    public String saveNewSubCategory(String parentCategory, List<CategoryModel> subCategories){
        Optional<Category> parent_Category=categoryRepository.findByName(parentCategory);

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
            return "Sub-category Added Successfully ";
        }
        else
            throw new ResourceNotFoundException("Parent Category does not exists");
    }

    public String updateCategory(CategoryModel categoryModel, String category){
        Optional<Category> category1=categoryRepository.findByName(category);

        if (category1.isPresent()){
            ModelMapper modelMapper = new ModelMapper();
            Category category2= modelMapper.map(categoryModel, Category.class);

            Category category3= category1.get();
            if (category2.getName()!=null)
                category3.setName(category2.getName());
            categoryRepository.save(category3);

            return "Category updated";
        }
        else
            throw new ResourceNotFoundException("Category name does not exist");
    }

}