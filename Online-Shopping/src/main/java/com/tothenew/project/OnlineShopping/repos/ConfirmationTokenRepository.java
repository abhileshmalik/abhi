package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Modifying
    @Query("from ConfirmationToken")
    ConfirmationToken deleteByConfirmationToken(String confirmationToken);

}