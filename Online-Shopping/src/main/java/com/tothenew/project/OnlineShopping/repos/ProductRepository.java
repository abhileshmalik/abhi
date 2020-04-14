package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("from Product")
    Product findByProductName(String name);


    @Query(value = "select * from product where category_id IN (select category_id from category where name = :category)" , nativeQuery = true)
    List<Product> findAllProducts(@Param("category") String category);

/*    @Query(value = "select * from category c, product p, product_variation pv where c.name=:category and c.category_id=p.category_id and p.product_id=pv.product_id" , nativeQuery = true)
    List<Product> findAllProducts(@Param("category") String category);*/


}
