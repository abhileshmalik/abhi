package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.dto.CustomerRegisterDto;
import com.tothenew.project.OnlineShopping.dto.SellerRegisterDto;
import com.tothenew.project.OnlineShopping.entities.*;
import com.tothenew.project.OnlineShopping.exception.TokenExpiredException;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.security.GrantAuthorityImpl;
import com.tothenew.project.OnlineShopping.tokens.ConfirmationToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    public List<Customer> findAllCustomers(String page, String size) {
        List<Customer> customers = (List<Customer>) userRepository.findCustomers(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
        return customers;
    }

    public List<Seller> findAllSellers(String page, String size) {
        List<Seller> sellers = (List<Seller>) userRepository.findSellers(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
        return sellers;
    }

    public User saveNewUser(User user) {
        String hpass = user.getPassword();
        user.setPassword(passwordEncoder.encode(hpass));
        user.setDeleted(false);
        user.setActive(true);
        user.setEnabled(true);
        user.setNonLocked(true);
        user.setAttempts(0);
        user.setRole("ROLE_ADMIN");
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
            return new AppUser(user.getUser_id(),user.getFirstName(),user.getUsername(), user.getPassword(),user.getEnabled(),user.getNonLocked(), Arrays.asList(new GrantAuthorityImpl(user.getRole())));
        } else {
            throw new RuntimeException();
        }

    }

    public String saveNewCustomer(CustomerRegisterDto customerRegisterDto) {

        User existingEmail = userRepository.findByEmailIgnoreCase(customerRegisterDto.getEmail());
        User existingUsername = userRepository.findByUsername(customerRegisterDto.getUsername());

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
            ModelMapper modelMapper = new ModelMapper();
            Customer customer= modelMapper.map(customerRegisterDto, Customer.class);

            String hpass = customer.getPassword();
            customer.setPassword(passwordEncoder.encode(hpass));
            customer.setDeleted(false);
            customer.setActive(true);
            customer.setEnabled(false);
            customer.setNonLocked(true);
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

    public String saveNewSeller(SellerRegisterDto sellerRegisterDto){

        User existingEmail = userRepository.findByEmailIgnoreCase(sellerRegisterDto.getEmail());
        User existingUsername = userRepository.findByUsername(sellerRegisterDto.getUsername());

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
            ModelMapper modelMapper = new ModelMapper();
            Seller seller= modelMapper.map(sellerRegisterDto, Seller.class);

            String hpass = seller.getPassword();
            seller.setPassword(passwordEncoder.encode(hpass));
            seller.setDeleted(false);
            seller.setActive(true);
            seller.setEnabled(false);
            seller.setNonLocked(true);
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

    @Transactional
    public String confirmUserAccount(String confirmationToken){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Date presentDate = new Date();
            if (token.getExpiryDate().getTime() - presentDate.getTime() <= 0) {
                throw new TokenExpiredException("Token has been expired");
            } else {
                User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
                user.setEnabled(true);
                userRepository.save(user);
                confirmationTokenRepository.deleteConfirmationToken(confirmationToken);

                return " Thank You," +
                        " Your account is successfully verified ";
            }
        }

        else {
            return "Error! Please try again";
        }
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
