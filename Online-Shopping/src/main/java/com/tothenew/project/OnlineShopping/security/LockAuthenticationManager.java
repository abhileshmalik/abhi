package com.tothenew.project.OnlineShopping.security;

import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;

@Configuration
public class LockAuthenticationManager implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();

        String password = authentication.getCredentials().toString();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username is not correct");

        if (user.getUsername().equals(username) && passwordEncoder.matches(password,user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        }
        else{
            Integer numberOfAttempts = user.getAttempts();
            user.setAttempts(++numberOfAttempts);
            userRepository.save(user);

            if (user.getAttempts() >= 3) {
                user.setNonLocked(false);
                userRepository.save(user);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Account Locked!");
                mailMessage.setText("Your account has been locked !! Go to this link to unlock your account - localhost:8080/account-unlock/{username}");
                emailService.sendEmail(mailMessage);

                throw new LockedException("User account is locked");
            }
            throw new BadCredentialsException("Password is incorrect");
        }
    }

    @Override
    public boolean supports(Class<?>aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
