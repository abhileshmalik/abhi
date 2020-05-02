package com.tothenew.project.OnlineShopping.security;

import com.tothenew.project.OnlineShopping.entities.AppUser;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

 /*   @Autowired
    PasswordEncoder passwordEncoder;*/

    @Autowired
    UserDaoService userDao;

    AppUser appUser;

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        //String encryptedPassword = passwordEncoder.encode("pass");
        System.out.println("Trying to authenticate user ::" + username);
        //System.out.println("Encrypted Password ::"+encryptedPassword);

        if (userDao.loadUserByUsername(username)==null)
        {
            throw new UserNotFoundException("Invalid Username entered");
        }
        else
        {
            appUser = userDao.loadUserByUsername(username);
            return appUser;
        }
       // System.out.println(appUser.getUid());
       // System.out.println(appUser.getName());


    }
}
