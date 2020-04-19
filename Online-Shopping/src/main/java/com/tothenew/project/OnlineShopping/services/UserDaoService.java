package com.tothenew.project.OnlineShopping.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.CustomerRegisterModel;
import com.tothenew.project.OnlineShopping.model.CustomerUpdateModel;
import com.tothenew.project.OnlineShopping.model.SellerRegisterModel;
import com.tothenew.project.OnlineShopping.entities.*;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.TokenExpiredException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.security.GrantAuthorityImpl;
import com.tothenew.project.OnlineShopping.tokens.ConfirmationToken;
import org.modelmapper.ModelMapper;
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

    public List<User> userList = new ArrayList<>();
    public List<Customer> customerList = new ArrayList<>();
    public List<Seller> sellerList = new ArrayList<>();
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
        return mapping3;
    }

    public MappingJacksonValue findAllSellers(String page, String size) {
        List<Seller> sellers = (List<Seller>) userRepository.findSellers(PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));

        SimpleBeanPropertyFilter filter4=SimpleBeanPropertyFilter.filterOutAllExcept("user_id","firstName","middleName",
                "lastName","email","isActive","companyName","companyContact","gstin", "isNonLocked", "isEnabled");

        FilterProvider filterProvider4=new SimpleFilterProvider().addFilter("userfilter",filter4);

        MappingJacksonValue mapping4=new MappingJacksonValue(sellers);
        mapping4.setFilters(filterProvider4);

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
        userList.add(user);
        userRepository.save(user);
        return user;
    }

    public AppUser loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
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
        User existingUsername = userRepository.findByUsername(customerRegisterModel.getUsername());

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
            mailMessage.setFrom("online-shopping@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            return "Registration Successful, Please verify your account via Activation link sent on your registered email";
        }
    }

    public String saveNewSeller(SellerRegisterModel sellerRegisterModel){

        User existingEmail = userRepository.findByEmailIgnoreCase(sellerRegisterModel.getEmail());
        User existingUsername = userRepository.findByUsername(sellerRegisterModel.getUsername());

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
            Seller seller= modelMapper.map(sellerRegisterModel, Seller.class);

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
            mailMessage.setFrom("online-shopping@gmail.com");
            mailMessage.setText("Hello Seller, Thank You for choosing Online-Shopping Portal." +
                    " Your has been registered successfully please wait for some time" +
                    " So that your account can be enabled by our team after verification  : ");

            emailSenderService.sendEmail(mailMessage);

            return "Registration Successful, ";
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

            if(customerUpdateModel.getContact() != null)
                customer1.setContact(customerUpdateModel.getContact());

            if (customerUpdateModel.getEmail() != null)
                customer1.setEmail(customerUpdateModel.getEmail());

            userRepository.save(customer1);
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
        Optional<User> id = userRepository.findById(user_id);

        if (address.isPresent()){
            addressRepository.deleteAdd(user_id, address_id);
            return "Address deleted";
        }
        else
            throw new ResourceNotFoundException("Invalid Address Id");
    }

    @Transactional
    @Modifying
    public  String updateAddress(AddressModel addressModel, Long addressId){
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()){
            Address savedAddress= address.get();

            if(addressModel.getAddressLine() != null)
                savedAddress.setAddressLine(addressModel.getAddressLine());

            if(addressModel.getCity() != null)
                savedAddress.setCity(addressModel.getCity());

            if(addressModel.getState() != null)
                savedAddress.setState(addressModel.getState());

            if(addressModel.getCountry() != null)
                savedAddress.setCountry(addressModel.getCountry());

            if(addressModel.getZipCode() != null)
                savedAddress.setZipCode(addressModel.getZipCode());

            if(addressModel.getLabel() != null)
                savedAddress.setLabel(addressModel.getLabel());

            return "Address updated";
        }
        else {
            throw new ResourceNotFoundException("Invalid Address Id");
        }

    }
}
