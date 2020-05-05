package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.orderprocessing.DailyOrderStatusEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyOrderStatusEmailRepository extends MongoRepository<DailyOrderStatusEmail,String> {


}
