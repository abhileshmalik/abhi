package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.ProductVariant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends CrudRepository<ProductVariant,String> {

}
