package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("from Product")
    Product findByProductName(String name);

    @Query(value = "select * from product where is_deleted=false AND is_active=true AND category_id IN (select category_id from category where name = :category)" ,
            nativeQuery = true)
    List<Product> findAllProducts(@Param("category") String category, Pageable pageable);

    @Query(value = "select * from product where seller_user_id=:sellerid",nativeQuery = true)
    List<Product> findSellerAssociatedProducts(@Param("sellerid") Long sellerid);

    @Query(value = "select * from product where category_id=:categoryid AND is_deleted=false AND is_active=true",
            nativeQuery = true)
    List<Product> findSimilar(@Param("categoryid") Long categoryid, Pageable pageable);


}
