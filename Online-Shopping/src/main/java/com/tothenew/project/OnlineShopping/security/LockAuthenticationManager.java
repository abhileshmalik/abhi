package com.tothenew.project.OnlineShopping.security;

import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LockAuthenticationManager implements ApplicationListener<AbstractAuthenticationEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent appEvent) {

        if (appEvent instanceof AuthenticationSuccessEvent)
        {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
            // add code here to handle successful login event
        }

        if (appEvent instanceof AuthenticationFailureBadCredentialsEvent) {
            AuthenticationFailureBadCredentialsEvent event = (AuthenticationFailureBadCredentialsEvent) appEvent;

            // add code here to handle unsuccessful login event
            // for example, counting the number of login failure attempts and storing it in db
            // this count can be used to lock or disable any user account as per business requirements
            String username = (String) event.getAuthentication().getPrincipal();
            User user = userRepository.findByUsernameIgnoreCase(username);
            if (user != null) {
                if (user.getAttempts() >= 2) {
                    user.setNonLocked(false);
                    SimpleMailMessage mailMessage=new SimpleMailMessage();
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setSubject("Account Locked !!");
                    mailMessage.setText("Your account has been locked due to Incorrect Password Attempts !! " +
                            " Go to this link to unlock your account" +
                            " - localhost:8080/account-unlock/{username}");
                    emailService.sendEmail(mailMessage);

                } else {
                    user.setAttempts(user.getAttempts() + 1);
                }
                userRepository.save(user);
            }
            else {
                throw new UsernameNotFoundException("Username is not correct");
            }
        }
    }
}
