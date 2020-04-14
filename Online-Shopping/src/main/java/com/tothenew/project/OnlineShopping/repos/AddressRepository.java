package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query(value = "select * from address where user_id=:id and label=:label", nativeQuery = true)
    Optional<Address> findByAdd(@Param("label") String label, @Param("id") Long id);

}
