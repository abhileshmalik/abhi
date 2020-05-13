package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccountUnlockService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(AccountUnlockService.class);


    public String unlockAccount(String username){

        User user=userRepository.findByUsername(username);
        if(user==null)
            throw new UserNotFoundException("User not found !!");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Unlock Account!!");
        mailMessage.setText("To unlock your account, please click here : "
                +"http://localhost:8080/do-unlock?username="+ username);
        emailSenderService.sendEmail(mailMessage);

        logger.info("********** Mail to unlock user account has been sent on registered emailId **********");

        return "Mail has been sent to you. Click on link to unlock your account";
    }

    @Transactional
    @Modifying
    public String unlockAccountSuccess(String username){

        User user=userRepository.findByUsername(username);
        if(user==null)
            throw new UserNotFoundException("User not found !!");

        user.setAttempts(0);
        user.setNonLocked(true);
        userRepository.save(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Unlock Successfull");
        mailMessage.setText("Your Account has been unlocked");
        emailSenderService.sendEmail(mailMessage);

        logger.info("********** Account Unlocked by User **********");

        return "Your account is unlocked now";
    }
}
