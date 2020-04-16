package com.tothenew.project.OnlineShopping.repos;

import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.tokens.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordRepository extends CrudRepository<ResetPasswordToken,Integer> {

    ResetPasswordToken findByUser(User user);

    ResetPasswordToken findByToken(String resetToken);

}
