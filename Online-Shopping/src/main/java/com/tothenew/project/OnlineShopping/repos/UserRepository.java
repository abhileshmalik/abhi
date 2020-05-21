package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

    @Query("from Customer")
    List<Customer> findCustomers(Pageable pageable);

    @Query("from Seller")
    List<Seller> findSellers(Pageable pageable);

    @Query("from Seller")
    List<Seller> findSellerList();


}
