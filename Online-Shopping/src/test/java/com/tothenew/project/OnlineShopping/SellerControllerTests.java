package com.tothenew.project.OnlineShopping;

import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.exception.ValidationException;
import com.tothenew.project.OnlineShopping.model.*;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.services.SellerDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SellerControllerTests {

    UserDaoService userDaoService = new UserDaoService();
    SellerDaoService sellerDaoService = new SellerDaoService();
    ProductDaoService productDaoService= new ProductDaoService();


    @Test
    void testCreateSeller() {

        SellerRegisterModel sellerRegisterModel = new SellerRegisterModel();

        //Given
        sellerRegisterModel.setUsername("sidd");
        sellerRegisterModel.setFirstName("siddharth");
        sellerRegisterModel.setMiddleName("k");
        sellerRegisterModel.setLastName("testing");
        sellerRegisterModel.setEmail("siddharth.bhatia@tothenew.com");
        sellerRegisterModel.setPassword("Abhi$9156");
        sellerRegisterModel.setConfirmPassword("Abhi$9156");
        sellerRegisterModel.setCompanyContact("9897654321");
        sellerRegisterModel.setCompanyName("TestingComp");
        sellerRegisterModel.setGstin("79ACXCE3618Q1ZX");

        Address address1 = new Address();
        address1.setAddressLine("Rohini");
        address1.setCity("New Delhi");
        address1.setState("Delhi");
        address1.setCountry("India");
        address1.setZipCode("147001");
        address1.setLabel("Home");

        //sellerRegisterModel.setAddresses();

        String expected = "Registration Successful";


        //When
        String actual = userDaoService.saveNewSeller(sellerRegisterModel);

        //Then
        assertEquals(expected,actual,"Seller Added");

    }

    @Test
    void testUpdateSellerProfile() {

        SellerUpdateModel sellerUpdateModel = new SellerUpdateModel();

        //Given
        Long id = 10L;
        sellerUpdateModel.setMiddleName("something");
        sellerUpdateModel.setCompanyContact("9988335566");

        String expected = "Profile updated successfully";

        //When
        String actual = sellerDaoService.updateSeller(sellerUpdateModel,id);

        //Then
        assertEquals(expected,actual,"Seller Profile Updated");

    }

    @Test
    void should_ThrowUserNotFoundException_when_InvalidSellerIdEntered_WhileUpdatingSeller() {

        SellerUpdateModel sellerUpdateModel = new SellerUpdateModel();

        //Given
        Long id = 101l;
        sellerUpdateModel.setMiddleName("something");
        sellerUpdateModel.setCompanyContact("9988335566");

        //When
        Executable executable = () -> sellerDaoService.updateSeller(sellerUpdateModel, id);

        //Then
        assertThrows(UserNotFoundException.class, executable);
    }


    @Test
    void testUpdateSellerAddress() {

        AddressModel addressModel = new AddressModel();

        //Given
        Long sellerid = 10L;
        Long addressId = 15L;

        addressModel.setAddressLine("A-67 Sec-11");
        addressModel.setCity("Noida");
        addressModel.setCity("U.P.");

        String expected = "Address updated";

        //When
        String actual = sellerDaoService.updateAddress(addressModel, addressId, sellerid);

        //Then
        assertEquals(expected,actual,"Seller Address Updated");

    }

    @Test
    void should_ThrowResourceNotFoundException_when_InvalidAddressIdEntered_WhileUpdatingSellerAddress() {

        AddressModel addressModel = new AddressModel();

        //Given
        Long sellerid = 10L;
        Long addressId = 105L;

        addressModel.setAddressLine("A-67 Sec-11");
        addressModel.setCity("Noida");
        addressModel.setCity("U.P.");

        //When
        Executable executable = () -> sellerDaoService.updateAddress(addressModel, addressId, sellerid);

        //Then
        assertThrows(ResourceNotFoundException.class, executable);
    }


    @Test
    void testUpdateCustomerPassword() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "sidd";

        updatePasswordModel.setOldPassword("Abhi$9156");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("abc123");

        String expected = "Password Updated Successfully";

        //When
        String actual = sellerDaoService.updateSellerPassword(updatePasswordModel, username);

        //Then
        assertEquals(expected,actual,"Seller Password Updated");

    }


    @Test
    void should_ThrowValidationException_when_OldPassword_dontMatch() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "sidd";

        updatePasswordModel.setOldPassword("Abi$916");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("abc123");

        //When
        Executable executable = () -> sellerDaoService.updateSellerPassword(updatePasswordModel, username);

        //Then
        assertThrows(ValidationException.class, executable);

    }

    @Test
    void should_ThrowValidationException_when_NewPassword_and_confirmPassword_dontMatch() {

        UpdatePasswordModel updatePasswordModel = new UpdatePasswordModel();

        //Given
        String username = "sidd";

        updatePasswordModel.setOldPassword("Abhi$9156");
        updatePasswordModel.setNewPassword("abc123");
        updatePasswordModel.setConfirmNewPassword("ab123");

        //When
        Executable executable = () -> sellerDaoService.updateSellerPassword(updatePasswordModel, username);

        //Then
        assertThrows(ValidationException.class, executable);

    }

    @Test
    void testUpdateProduct() {

        ProductUpdateModel productUpdateModel = new ProductUpdateModel();

        //Given
        Long pid = 6l;
        Long sellerid = 11l;

        productUpdateModel.setProductName("TestProduct1");
        productUpdateModel.setProductDescription("New Test Product-Description");

        String expected = "Product Updated Successfully";

        //When
        String actual = productDaoService.updateProduct(productUpdateModel, pid, sellerid);

        //Then
        assertEquals(expected,actual,"Product Updated");

    }

    @Test
    void should_throwResourceNotFoundException_ifInvalidProductIdPassed_inUpdateProduct() {

        ProductUpdateModel productUpdateModel = new ProductUpdateModel();

        //Given
        Long pid = 6l;
        Long sellerid = 101l;

        productUpdateModel.setProductName("TestProduct1");
        productUpdateModel.setProductDescription("New Test Product-Description");

        //When
        Executable executable = () -> productDaoService.updateProduct(productUpdateModel, pid, sellerid);

        //Then
        assertThrows(ResourceNotFoundException.class, executable);

    }

    @Test
    void testDeleteProduct() {

        //Given
        Long pid = 6l;
        Long sellerid = 11l;

        String expected = "Product Deleted Successfully";

        //When
        String actual = productDaoService.deleteProduct(pid, sellerid);

        //Then
        assertEquals(expected,actual,"Product Deleted");

    }

    @Test
    void should_throwResourceNotFoundException_ifInvalidProductIdPassed_inDeleteProduct() {

        //Given
        Long pid = 6l;
        Long sellerid = 101l;

        //When
        Executable executable = () -> productDaoService.deleteProduct(pid, sellerid);

        //Then
        assertThrows(ResourceNotFoundException.class, executable);

    }


}
