package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.Category;
import com.tothenew.project.OnlineShopping.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    @Query(value = "select name from category where name=:cname", nativeQuery = true)
    String findByCatName(@Param("cname") String cname);

    @Query(value = "select * from category where parent_id is not NULL", nativeQuery = true)
    List<Category> findAllSubCategories();

    @Query(value = "select * from category where parent_id=:parent", nativeQuery = true)
    List<Category> findchildCategoriesOfParent(@Param("parent") Long parent);
}
