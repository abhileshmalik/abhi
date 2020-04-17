package com.tothenew.project.OnlineShopping.security;

import com.tothenew.project.OnlineShopping.entities.AppUser;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

 /*   @Autowired
    PasswordEncoder passwordEncoder;*/

    @Autowired
    UserDaoService userDao;

    AppUser appUser;

    @Override
    public AppUser loadUserByUsername(String username) throws UserNotFoundException {
        //String encryptedPassword = passwordEncoder.encode("pass");
        System.out.println("Trying to authenticate user ::" + username);
        //System.out.println("Encrypted Password ::"+encryptedPassword);
        appUser = userDao.loadUserByUsername(username);
       // System.out.println(appUser.getUid());
       // System.out.println(appUser.getName());

        return appUser;
    }
}
