package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.entities.*;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.security.GrantAuthorityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDaoService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public List<User> userList = new ArrayList<>();
    public List<Customer> customerList = new ArrayList<>();
    public List<Seller> sellerList = new ArrayList<>();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    public List<Customer> findAllCustomers() {
        List<Customer> customers = (List<Customer>) userRepository.findCustomers();
        return customers;
    }

    public List<Seller> findAllSellers() {
        List<Seller> sellers = (List<Seller>) userRepository.findSellers();
        return sellers;
    }

    public User saveNewUser(User user) {
        String hpass = user.getPassword();
        user.setPassword(passwordEncoder.encode(hpass));
        user.setDeleted(false);
        user.setActive(true);
        user.setEnabled(true);
        user.setNonLockedLocked(true);

        userList.add(user);
        userRepository.save(user);
        return user;
    }

/*    public Customer createNewCustomer (Customer customer1) {
        String hpass = customer1.getPassword();
        customer1.setPassword(passwordEncoder.encode(hpass));
        customer1.setRole("ROLE_USER");
        customerList.add(customer1);
        userRepository.save(customer1);
        return customer1;
    }*/

    public AppUser loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        System.out.println(user);
        if (username != null) {
            return new AppUser(user.getUser_id(),user.getFirstName(),user.getUsername(), user.getPassword(),user.getEnabled(),user.getNonLockedLocked(), Arrays.asList(new GrantAuthorityImpl(user.getRole())));
        } else {
            throw new RuntimeException();
        }

    }

    public String saveNewCustomer(Customer customer) {

        User existingEmail = userRepository.findByEmailIgnoreCase(customer.getEmail());
        User existingUsername = userRepository.findByUsername(customer.getUsername());

        if(existingEmail != null)
        {
            return "This email ID is already registered with us";
        }
        else if (existingUsername != null) {
            {
                return "This username is already taken please try something else";
            }
        }
        else
        {
            String hpass = customer.getPassword();
            customer.setPassword(passwordEncoder.encode(hpass));
            customer.setDeleted(false);
            customer.setActive(true);
            customer.setEnabled(false);
            customer.setNonLockedLocked(true);
            customer.setRole("ROLE_USER");

            userRepository.save(customer);

            ConfirmationToken confirmationToken = new ConfirmationToken(customer);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(customer.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("online-shopping@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            return "Registration Successful, Please verify your account via Activation link sent on your registered email";
        }
    }

    public String saveNewSeller(Seller seller){

        User existingEmail = userRepository.findByEmailIgnoreCase(seller.getEmail());
        User existingUsername = userRepository.findByUsername(seller.getUsername());

        if(existingEmail != null)
        {
            return "This email ID is already registered with us";
        }
        else if (existingUsername != null) {
            {
                return "This username is already taken please try something else";
            }
        }
        else
        {
            String hpass = seller.getPassword();
            seller.setPassword(passwordEncoder.encode(hpass));
            seller.setDeleted(false);
            seller.setActive(true);
            seller.setEnabled(false);
            seller.setNonLockedLocked(true);
            seller.setRole("ROLE_SELLER");

            userRepository.save(seller);

            ConfirmationToken confirmationToken = new ConfirmationToken(seller);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(seller.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("online-shopping@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            return "Registration Successful, Please verify your account via Activation link sent on your registered email";
        }
    }


    public String confirmUserAccount(String confirmationToken){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);

            //confirmationTokenRepository.deleteByConfirmationToken(confirmationToken);  //Deleting token for particular user

            userRepository.save(user);

            deleteToken(confirmationToken);

            return " Thank You," +
                    " Your account is successfully verified ";

        }
        else {
            return "Error! Please try again";
        }
    }

    @Modifying
    @Transactional
    public void deleteToken(String confirmationToken) {
        confirmationTokenRepository.deleteByConfirmationToken(confirmationToken);
    }

    public Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String username = appUser.getUsername();
        Customer customer = (Customer) userRepository.findByUsername(username);
        return customer;
    }

    public Seller getLoggedInSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String username = appUser.getUsername();
        Seller seller = (Seller) userRepository.findByUsername(username);
        return seller;
    }
}
