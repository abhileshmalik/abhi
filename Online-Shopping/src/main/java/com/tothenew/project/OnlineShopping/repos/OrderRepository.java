package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.orderprocessing.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Long> {

}
