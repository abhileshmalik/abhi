package com.tothenew.project.OnlineShopping.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.exception.*;
import com.tothenew.project.OnlineShopping.model.*;
import com.tothenew.project.OnlineShopping.entities.*;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.security.GrantAuthorityImpl;
import com.tothenew.project.OnlineShopping.tokens.ConfirmationToken;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserDaoService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(UserDaoService.class);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<User> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    public MappingJacksonValue findAllCustomers(String page, String size) {
        List<Customer> customers = (List<Customer>) userRepository.findCustomers(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));

        SimpleBeanPropertyFilter filter3=SimpleBeanPropertyFilter.filterOutAllExcept("user_id","firstName","middleName",
                "lastName","email","isActive","contact", "isNonLocked", "isEnabled");

        FilterProvider filterProvider4=new SimpleFilterProvider().addFilter("userfilter",filter3);

        MappingJacksonValue mapping3=new MappingJacksonValue(customers);
        mapping3.setFilters(filterProvider4);

        logger.info("********** All Customers List Retrieved **********");

        return mapping3;
    }

    public MappingJacksonValue findAllSellers(String page, String size) {
        List<Seller> sellers = (List<Seller>) userRepository.findSellers(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));

        SimpleBeanPropertyFilter filter4=SimpleBeanPropertyFilter.filterOutAllExcept("user_id","firstName","middleName",
                "lastName","email","isActive","companyName","companyContact","gstin", "isNonLocked", "isEnabled");

        FilterProvider filterProvider4=new SimpleFilterProvider().addFilter("userfilter",filter4);

        MappingJacksonValue mapping4=new MappingJacksonValue(sellers);
        mapping4.setFilters(filterProvider4);

        logger.info("********** All Sellers List Retrieved **********");

        return mapping4;
    }

    // Can be used for creating Admin via URL
    public User saveNewUser(User user) {
        String hpass = user.getPassword();
        user.setPassword(passwordEncoder.encode(hpass));
        user.setDeleted(false);
        user.setActive(true);
        user.setEnabled(true);
        user.setNonLocked(true);
        user.setAttempts(0);
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
        return user;
    }

    public AppUser loadUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        System.out.println(user);
        if(user!= null) {
            if (username != null) {
                return new AppUser(user.getUser_id(), user.getFirstName(), user.getUsername(), user.getPassword(), user.getEnabled(), user.getNonLocked(), Arrays.asList(new GrantAuthorityImpl(user.getRole())));
            }
            else {
                throw new UsernameNotFoundException("Username cannot be blank...");
            }
        } else
        {
            throw new UserNotFoundException("Invalid Username Entered");
        }
    }

    public String saveNewCustomer(CustomerRegisterModel customerRegisterModel) {

        User existingEmail = userRepository.findByEmailIgnoreCase(customerRegisterModel.getEmail());
        User existingUsername = userRepository.findByUsernameIgnoreCase(customerRegisterModel.getUsername());

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
                Customer customer= modelMapper.map(customerRegisterModel, Customer.class);

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
                mailMessage.setFrom("procart2020@gmail.com");
                mailMessage.setText("To confirm your account, please click here : "
                        +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

                emailSenderService.sendEmail(mailMessage);

            logger.info("********** New Customer Registered **********");

                return "Registration Successful, Please verify your account via Activation link sent on your registered email";
            }
    }

    public String saveNewSeller(SellerRegisterModel sellerRegisterModel) {

        User existingEmail = userRepository.findByEmailIgnoreCase(sellerRegisterModel.getEmail());
        User existingUsername = userRepository.findByUsernameIgnoreCase(sellerRegisterModel.getUsername());

        if (existingEmail != null) {
            return "This email ID is already registered with us";
        } else if (existingUsername != null) {
            {
                return "This username is already taken please try something else";
            }
        } else {
            ModelMapper modelMapper = new ModelMapper();
            Seller seller = modelMapper.map(sellerRegisterModel, Seller.class);

            String hpass = seller.getPassword();
            seller.setPassword(passwordEncoder.encode(hpass));
            seller.setDeleted(false);
            seller.setActive(true);
            seller.setEnabled(false);
            seller.setNonLocked(true);
            seller.setRole("ROLE_SELLER");

            userRepository.save(seller);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(seller.getEmail());
            mailMessage.setSubject("Registration Successful!");
            mailMessage.setFrom("procart2020@gmail.com");
            mailMessage.setText("Hello Seller, Thank You for choosing Pro-Cart." +
                    " Your has been registered successfully please wait for some time" +
                    " So that your account can be enabled by our team after verification ");

            emailSenderService.sendEmail(mailMessage);

            logger.info("********** New Seller Registered **********");

            return "Registration Successful";
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

    @Transactional
    public String enableSellerAccount(Long sellerId){
        Optional<User> user = userRepository.findById(sellerId);

        if (user.isPresent()) {
            User user1 = user.get();
            if(user1.getRole().equals("ROLE_SELLER")) {
                Seller seller = new Seller();
                seller = (Seller) user.get();

                if (!seller.getEnabled()) {
                    seller.setEnabled(true);
                    userRepository.save(seller);

                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(seller.getEmail());
                    mailMessage.setSubject("Account Enabled");
                    mailMessage.setText("Your account has been enabled by admin.");
                    emailSenderService.sendEmail(mailMessage);

                    return "Seller Account Enabled successfully";
                }
                else
                {
                    return "Seller Already Enabled";
                }
            }
            else {
                throw new BadRequestException("Entered ID is not a SellerID but a customer ID");

            }
        }
        else
            throw new UserNotFoundException("Seller ID not found");
    }


    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String username = appUser.getUsername();
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String username = appUser.getUsername();
        return (Customer) userRepository.findByUsernameIgnoreCase(username);
    }

    public Seller getLoggedInSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String username = appUser.getUsername();
        return (Seller) userRepository.findByUsernameIgnoreCase(username);
    }


    @Transactional
    @Modifying
    public String updateCustomer(CustomerUpdateModel customerUpdateModel, Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            Customer customer1= (Customer) user.get();

            if(customerUpdateModel.getFirstName() != null)
                customer1.setFirstName(customerUpdateModel.getFirstName());

            if(customerUpdateModel.getMiddleName() != null)
                customer1.setMiddleName(customerUpdateModel.getMiddleName());

            if(customerUpdateModel.getLastName() != null)
                customer1.setLastName(customerUpdateModel.getLastName());

            if(customerUpdateModel.getContact() != null) {
                if (!customerUpdateModel.getContact().matches("^[0-9]*$"))
                    throw new ValidationException("Phone number must contain numbers only");
                customer1.setContact(customerUpdateModel.getContact());
            }

            if (customerUpdateModel.getEmail() != null) {
                if (!customerUpdateModel.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$"))
                    throw new ValidationException("Email ID must be valid! ");
                if ((userRepository.findByEmailIgnoreCase(customerUpdateModel.getEmail()) != null))
                    throw new NotUniqueException("Email ID already exists");
                customer1.setEmail(customerUpdateModel.getEmail());
            }

            userRepository.save(customer1);

            logger.info("********** Customer Profile Updated **********");

            return "Profile updated successfully";
        }
        else
            throw new UserNotFoundException("User not found");

    }


    @Transactional
    @Modifying
    public String addAddress(AddressModel addressModel, Long id){

        Optional<User> user = userRepository.findById(id);
        User user1= user.get();

        Address address = new Address();
        address.setAddressLine(addressModel.getAddressLine());
        address.setCity(addressModel.getCity());
        address.setState(addressModel.getState());
        address.setCountry(addressModel.getCountry());
        address.setZipCode(addressModel.getZipCode());
        address.setLabel(addressModel.getLabel());
        address.setUser(user1);
        addressRepository.save(address);
        return "Address added";
    }

    @Transactional
    @Modifying
    public String deleteAddress(Long address_id, Long user_id){
        Optional<Address> address = addressRepository.findById(address_id);

        if (address.isPresent()){
            addressRepository.deleteAdd(user_id, address_id);

            logger.info("********** Address Deleted **********");

            return "Address deleted";
        }
        else
            throw new ResourceNotFoundException("Invalid Address Id");
    }

    @Transactional
    @Modifying
    public  String updateAddress(AddressModel addressModel, Long addressId, Long user_id){
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            Address savedAddress = address.get();

            if (savedAddress.getUser().getUser_id().equals(user_id)) {

                if (addressModel.getAddressLine() != null)
                    savedAddress.setAddressLine(addressModel.getAddressLine());

                if (addressModel.getCity() != null)
                    savedAddress.setCity(addressModel.getCity());

                if (addressModel.getState() != null)
                    savedAddress.setState(addressModel.getState());

                if (addressModel.getCountry() != null)
                    savedAddress.setCountry(addressModel.getCountry());

                if (addressModel.getZipCode() != null)
                    savedAddress.setZipCode(addressModel.getZipCode());

                if (addressModel.getLabel() != null)
                    savedAddress.setLabel(addressModel.getLabel());

                logger.info("********** Address Updated **********");

                return "Address updated";
            }
            else
            {
                throw new BadRequestException("Address not associated to the Logged in customer");
            }
        }

        else {
            throw new ResourceNotFoundException("Invalid Address Id");
        }

    }

    @Transactional
    public String resendActivationToken(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user!=null) {
            if (!user.getEnabled()) {

            ConfirmationToken token = null;
            token = confirmationTokenRepository.findByUser(user);

                if (token != null) {
                    String confirmationToken = token.getConfirmationToken();
                    Date presentDate = new Date();
                        if (token.getExpiryDate().getTime() - presentDate.getTime() <= 0) {

                            confirmationTokenRepository.deleteConfirmationToken(confirmationToken);   // Delete old Token

                            ConfirmationToken newConfirmationToken = new ConfirmationToken(user);    // Generate New Token

                            confirmationTokenRepository.save(newConfirmationToken);

                            SimpleMailMessage mailMessage = new SimpleMailMessage();
                            mailMessage.setTo(user.getEmail());
                            mailMessage.setSubject("Complete Registration");
                            mailMessage.setText("To activate your account, please click here : "
                            + "http://localhost:8080/confirm-account?token=" + newConfirmationToken.getConfirmationToken());

                            emailSenderService.sendEmail(mailMessage);

                            logger.info("********** New Activation Token Generated for the user **********");

                            return "New Activation Link sent successfully on your registered email";

                        }
                    else {
                        return "Your current Activation Link sent via Email is not expired yet," +
                            " please use the same old link to Enable your account ";
                    }
                }
            else {
                ConfirmationToken newConfirmationToken = new ConfirmationToken(user);    // Generate New Token
                confirmationTokenRepository.save(newConfirmationToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Complete Registration");
                mailMessage.setText("To activate your account, please click here : "
                        + "http://localhost:8080/confirm-account?token=" + newConfirmationToken.getConfirmationToken());

                emailSenderService.sendEmail(mailMessage);

                logger.info("********** New Activation Token Generated for the user **********");

                return "New Activation Link sent successfully on your registered email";
            }
        }
            else
            {
                return "No need to generate any token your Account is already enabled," +
                        " Try to login using your credentials.";
            }
        }
        else {
            throw new UserNotFoundException("Invalid EmailID entered");
        }
    }

    public String updateCustomerPassword(UpdatePasswordModel updatePasswordModel, String username) {

        User user = userRepository.findByUsernameIgnoreCase(username);

        String oldPassword = updatePasswordModel.getOldPassword();

        if (passwordEncoder.matches(oldPassword,user.getPassword())){

            String newPassword = updatePasswordModel.getNewPassword();
            String confirmNewPassword = updatePasswordModel.getConfirmNewPassword();

            if (newPassword.equals(confirmNewPassword)) {

                String newpass = passwordEncoder.encode(updatePasswordModel.getNewPassword());

                user.setPassword(newpass);
                userRepository.save(user);

                String emailId = user.getEmail();
                String subject = "Password Updated !!";
                String text = "Your account password has been changed recently," +
                        " if you have not done this kindly report it to our team.";
                emailSenderService.sendEmail(emailId, subject, text);

                logger.info("********** Customer Password Updated **********");

                return "Password Updated Successfully";
            }
            else
            {
                throw new ValidationException("password and confirm password not matched !");
            }
        }
        else {
            throw new ValidationException("Old password does not match with our records");
        }
    }


}
