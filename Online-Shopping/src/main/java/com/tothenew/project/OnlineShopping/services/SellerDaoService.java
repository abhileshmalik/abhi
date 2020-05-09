package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.*;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.SellerUpdateModel;
import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.model.UpdatePasswordModel;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SellerDaoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    Logger logger = LoggerFactory.getLogger(SellerDaoService.class);


    @Transactional
    @Modifying
    public String updateSeller(SellerUpdateModel sellerUpdateModel, Long id){
        Optional<User> seller = userRepository.findById(id);

        if (seller.isPresent()){
            Seller seller1= (Seller) seller.get();

            if(sellerUpdateModel.getFirstName() != null)
                seller1.setFirstName(sellerUpdateModel.getFirstName());

            if(sellerUpdateModel.getMiddleName() != null)
                seller1.setMiddleName(sellerUpdateModel.getMiddleName());

            if(sellerUpdateModel.getLastName() != null)
                seller1.setLastName(sellerUpdateModel.getLastName());

            if (sellerUpdateModel.getEmail() != null) {
                if (!sellerUpdateModel.getEmail().matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$"))
                    throw new ValidationException("Email ID must be valid! ");
                if ((userRepository.findByEmailIgnoreCase(sellerUpdateModel.getEmail()) != null))
                    throw new NotUniqueException("Email ID already exists");
                seller1.setEmail(sellerUpdateModel.getEmail());
            }

            if(sellerUpdateModel.getGstin() != null)
                seller1.setGstin(sellerUpdateModel.getGstin());

            if (sellerUpdateModel.getCompanyName() != null)
                seller1.setCompanyName(sellerUpdateModel.getCompanyName());

            if (sellerUpdateModel.getCompanyContact() != null) {
                if (!sellerUpdateModel.getCompanyContact().matches("^[0-9]*$"))
                    throw new ValidationException("Phone number must contain numbers only");
                seller1.setCompanyContact(sellerUpdateModel.getCompanyContact());
            }

            userRepository.save(seller1);

            logger.info("********** Seller Profile Updated **********");

            return "Profile updated successfully";
        }
        else
            throw new UserNotFoundException("User not found");

    }

    @Transactional
    @Modifying
    public  String updateAddress(AddressModel addressModel, Long addressId, Long sellerid ){
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            Address savedAddress = address.get();

            Long s_id = savedAddress.getUser().getUser_id();

            if (s_id.equals(sellerid)) {
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
            else {
                throw new BadRequestException("Address not associated to current seller");
            }
        }
        else {
            throw new ResourceNotFoundException("Invalid Address Id");
        }
    }

    public String updateSellerPassword(UpdatePasswordModel updatePasswordModel, String username) {

        User user = userRepository.findByUsername(username);

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

                logger.info("********** Seller Password Updated **********");

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
