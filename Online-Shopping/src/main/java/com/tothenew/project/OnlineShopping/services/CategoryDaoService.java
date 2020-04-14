package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Category saveNewCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public List<Category> saveNewSubCategory(String parentCategory, List<Category> subCategory){
        Optional<Category> parent_Category=categoryRepository.findByName(parentCategory);

        Category category=new Category();
        category=parent_Category.get();

        //category.addSubCategory(subCategory);

        Category finalCategory = category;
        subCategory.forEach(e->e.setSubcategory(finalCategory));

        categoryRepository.saveAll(subCategory);
        return subCategory;
    }

}