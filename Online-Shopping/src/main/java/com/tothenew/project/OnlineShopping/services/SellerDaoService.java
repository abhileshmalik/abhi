package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.BadRequestException;
import com.tothenew.project.OnlineShopping.model.AddressModel;
import com.tothenew.project.OnlineShopping.model.SellerUpdateModel;
import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SellerDaoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;


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

            if (sellerUpdateModel.getEmail() != null)
                seller1.setEmail(sellerUpdateModel.getEmail());

            if(sellerUpdateModel.getGstin() != null)
                seller1.setGstin(sellerUpdateModel.getGstin());

            if (sellerUpdateModel.getCompanyName() != null)
                seller1.setCompanyName(sellerUpdateModel.getCompanyName());

            if (sellerUpdateModel.getCompanyContact() != null)
                seller1.setCompanyContact(sellerUpdateModel.getCompanyContact());

            userRepository.save(seller1);
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
}
