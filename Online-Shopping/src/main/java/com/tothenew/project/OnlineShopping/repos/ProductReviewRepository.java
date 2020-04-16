package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.product.ProductReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends CrudRepository<ProductReview, Long> {

}
