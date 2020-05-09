package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.orderprocessing.DailyOrderStatusEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
public interface DailyOrderStatusEmailRepository extends MongoRepository<DailyOrderStatusEmail,String> {


}
