package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminDaoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Transactional
    public String activateUser(Long uid) {

        Optional<User> user1 = userRepository.findById(uid);
        if (user1.isPresent()) {
            User user = user1.get();

            if (!user.getActive()) {                    //Check if its false
                user.setActive(true);
                userRepository.save(user);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Account Activated!!");
                mailMessage.setFrom("wishcart@gmail.com");
                mailMessage.setText("Your account is successfully activated........ " +
                        "Thank You for Choosing WishCart");
                emailSenderService.sendEmail((mailMessage));

                return "User Activated";
            } else {
                return "User is already activated";
            }

        } else {
            throw new UserNotFoundException("Incorrect User ID");
        }
    }

    @Transactional
    public String deactivateUser(Long uid)
    {
        Optional<User> user1 = userRepository.findById(uid);
        if (user1.isPresent()) {

            User user = user1.get();
            if(user.getActive())
            {
                user.setActive(false);
                userRepository.save(user);
                SimpleMailMessage mailMessage=new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Account Deactivated!!");
                mailMessage.setFrom("wishcart@gmail.com");
                mailMessage.setText("Your account has been deactivated due to non usage/compliance violation ........ " +
                        "Please contact wishcart for assistance");
                emailSenderService.sendEmail((mailMessage));
                return "User Deactivated";
            }
            else
            {
                return "User is already deactivated";
            }

        } else {
            throw new UserNotFoundException("Incorrect User ID");
        }
    }


}
