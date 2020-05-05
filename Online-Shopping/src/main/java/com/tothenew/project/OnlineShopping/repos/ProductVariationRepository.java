package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.ProductVariation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

    @Query(value = "select * from product_variation where product_id =:productId", nativeQuery = true)
    List<ProductVariation> findByProductId(@Param("productId") Long productId);


}
