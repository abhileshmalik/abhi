package com.tothenew.project.OnlineShopping.entities;

import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //Admin Creation
        User user = new User();
        user.setUser_id(1L);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("adminpass"));
        user.setEmail("admin@tothenew.com");
        user.setFirstName("admin");
        user.setLastName("xyz");
        user.setActive(true);
        user.setEnabled(true);
        user.setNonLocked(true);
        user.setDeleted(false);
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);*/

    }
}
