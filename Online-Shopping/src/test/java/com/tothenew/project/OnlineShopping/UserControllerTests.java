package com.tothenew.project.OnlineShopping;

import com.tothenew.project.OnlineShopping.controller.UserController;
import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.ValidationException;
import com.tothenew.project.OnlineShopping.model.*;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class UserControllerTests {

   // UserController userController = new UserController();
    UserDaoService userDaoService = new UserDaoService();

    @Test
    void testCreateCustomer() {

        CustomerRegisterModel customerRegisterModel = new CustomerRegisterModel();

        //Given
        customerRegisterModel.setUsername("abhi03");
        customerRegisterModel.setFirstName("abhi");
        customerRegisterModel.setMiddleName("");
        customerRegisterModel.setLastName("testing");
        customerRegisterModel.setEmail("abhileshmalik03@gmail.com");
        customerRegisterModel.setPassword("Abhi$9156");
        customerRegisterModel.setConfirmPassword("Abhi$9156");
        customerRegisterModel.setContact("9897654321");

        Address address1 = new Address();
        address1.setAddressLine("Rohini");
        address1.setCity("New Delhi");
        address1.setState("Delhi");
        address1.setCountry("India");
        address1.setZipCode("147001");
        address1.setLabel("Home");

        Address address2 = new Address();
        address2.setAddressLine("C-4/E Janakpuri");
        address2.setCity("New Delhi");
        address2.setState("Delhi");
        address2.setCountry("India");
        address2.setZipCode("147001");
        address2.setLabel("Home2");

        Set addresses = new HashSet<>();
        addresses.add(address1);
        addresses.add(address2);

        // customerRegisterModel.setAddresses();

        String expected = "Registration Successful, Please verify your account via Activation link sent on your registered email";


        //When
         String actual = userDaoService.saveNewCustomer(customerRegisterModel);

         //Then
        assertEquals(expected,actual,"Customer Added");

    }

    @Test
    void testUpdateCustomerProfile() {

        CustomerUpdateModel customerUpdateModel = new CustomerUpdateModel();

        //Given
        Long id = 10L;
        customerUpdateModel.setMiddleName("something");
        customerUpdateModel.setContact("7011567378");

        String expected = "Profile updated successfully";

        //When
        String actual = userDaoService.updateCustomer(customerUpdateModel,id);

        //Then
        assertEquals(expected,actual,"Customer Profile Updated");

    }

    @Test
    void testUpdateCustomerAddress() {

        AddressModel addressModel = new AddressModel();

        //Given
        Long user_id = 10L;
        Long addressId = 15L;

        addressModel.setAddressLine("A-67 Sec-11");
        addressModel.setCity("Noida");
        addressModel.setCity("U.P.");

        String expected = "Address updated";

        //When
        String actual = userDaoService.updateAddress(addressModel, addressId, user_id);

        //Then
        assertEquals(expected,actual,"Customer Address Updated");

    }

    @Test
    void testUpdateCustomerPassword() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "abhi03";

        updatePasswordModel.setOldPassword("Abhi$9156");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("abc123");

        String expected = "Password Updated Successfully";

        //When
        String actual = userDaoService.updateCustomerPassword(updatePasswordModel, username);

        //Then
        assertEquals(expected,actual,"Customer Password Updated");

    }

    @Test
    void should_ThrowValidationException_when_OldPassword_dontMatch() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "abhi03";

        updatePasswordModel.setOldPassword("Abi$916");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("abc123");

        //When
        Executable executable = () -> userDaoService.updateCustomerPassword(updatePasswordModel, username);

        //Then
        assertThrows(ValidationException.class, executable);

    }

    @Test
    void should_ThrowValidationException_when_NewPassword_and_confirmPassword_dontMatch() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "abhi03";

        updatePasswordModel.setOldPassword("Abhi$9156");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("ab123");

        //When
        Executable executable = () -> userDaoService.updateCustomerPassword(updatePasswordModel, username);

        //Then
        assertThrows(ValidationException.class, executable);

    }

    @Test
    void testDeleteAddress() {

        //Given
        Long user_id = 10L;
        Long addressId = 15L;

        String expected = "Address deleted";

        //When
        String actual = userDaoService.deleteAddress(addressId, user_id);

        //Then
        assertEquals(expected,actual,"Customer Address Deleted");

    }

    @Test
    void testAddCustomerAddress() {

        AddressModel addressModel = new AddressModel();

        //Given
        Long id = 10L;

        addressModel.setAddressLine("Rohini");
        addressModel.setCity("New Delhi");
        addressModel.setState("Delhi");
        addressModel.setCountry("India");
        addressModel.setZipCode("147001");
        addressModel.setLabel("Home");

        String expected = "Address added";

        //When
        String actual = userDaoService.addAddress(addressModel, id);

        //Then
        assertEquals(expected,actual,"Customer Address Added");

    }

    @Test
    void should_ThrowResourceNotFoundException_when_InvalidAddressIdEntered_WhileUpdatingCustomerAddress() {

        AddressModel addressModel = new AddressModel();

        //Given
        Long sellerid = 10L;
        Long addressId = 105L;

        addressModel.setAddressLine("A-67 Sec-11");
        addressModel.setCity("Noida");
        addressModel.setCity("U.P.");

        //When
        Executable executable = () -> userDaoService.updateAddress(addressModel, addressId, sellerid);

        //Then
        assertThrows(ResourceNotFoundException.class, executable);
    }






}
