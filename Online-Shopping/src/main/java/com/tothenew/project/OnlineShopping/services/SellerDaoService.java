package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.dto.AddressDto;
import com.tothenew.project.OnlineShopping.dto.SellerUpdateDto;
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
    public String updateSeller(SellerUpdateDto sellerUpdateDto, Long id){
        Optional<User> seller = userRepository.findById(id);

        if (seller.isPresent()){
            Seller seller1= (Seller) seller.get();

            if (sellerUpdateDto.getUsername() != null)
                seller1.setUsername(sellerUpdateDto.getUsername());

            if(sellerUpdateDto.getFirstName() != null)
                seller1.setFirstName(sellerUpdateDto.getFirstName());

            if(sellerUpdateDto.getMiddleName() != null)
                seller1.setMiddleName(sellerUpdateDto.getMiddleName());

            if(sellerUpdateDto.getLastName() != null)
                seller1.setLastName(sellerUpdateDto.getLastName());

            if (sellerUpdateDto.getEmail() != null)
                seller1.setEmail(sellerUpdateDto.getEmail());

            if(sellerUpdateDto.getGstin() != null)
                seller1.setGstin(sellerUpdateDto.getGstin());

            if (sellerUpdateDto.getCompanyName() != null)
                seller1.setCompanyName(sellerUpdateDto.getCompanyName());

            if (sellerUpdateDto.getCompanyContact() != null)
                seller1.setCompanyContact(sellerUpdateDto.getCompanyContact());

            userRepository.save(seller1);
            return "Profile updated successfully";
        }
        else
            throw new UserNotFoundException("User not found");

    }

    @Transactional
    @Modifying
    public  String updateAddress(AddressDto addressDto , Long addressId){
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()){
            Address savedAddress= address.get();

            if(addressDto.getAddressLine() != null)
                savedAddress.setAddressLine(addressDto.getAddressLine());

            if(addressDto.getCity() != null)
                savedAddress.setCity(addressDto.getCity());

            if(addressDto.getState() != null)
                savedAddress.setState(addressDto.getState());

            if(addressDto.getCountry() != null)
                savedAddress.setCountry(addressDto.getCountry());

            if(addressDto.getZipCode() != null)
                savedAddress.setZipCode(addressDto.getZipCode());

            if(addressDto.getLabel() != null)
                savedAddress.setLabel(addressDto.getLabel());

            return "Address updated";
        }
        else {
            throw new ResourceNotFoundException("Invalid Address Id");
        }

    }
}
