package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.orderprocessing.Wishlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Long> {


}
