package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.ProductVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

}
