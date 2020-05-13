package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.orderprocessing.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Long> {

    @Modifying
    @Query(value = "delete from wishlist where customer_user_id=:customerid AND product_variation_id=:vid", nativeQuery = true)
    void deleteWishlistItem(@Param("customerid") Long customerid, @Param("vid") Long vid);

}
