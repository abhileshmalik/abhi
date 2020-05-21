package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.exception.ValidationException;
import com.tothenew.project.OnlineShopping.model.*;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.services.EmailSenderService;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.services.SellerDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {SellerController.class, SellerDaoService.class, UserDaoService.class, EmailSenderService.class})
@WebMvcTest(SellerController.class)
public class SellerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "tokenStore")
    private TokenStore tokenStore;

    @MockBean(name = "messageSource")
    MessageSource messageSource;

    @MockBean(name = "javaMailSender")
    JavaMailSenderImpl javaMailSender;

    @MockBean(name = "userRepository")                          // use to Mockout that module/controller or repository
    private UserRepository userRepository;

    @MockBean(name = "addressRepository")
    private AddressRepository addressRepository;

    @MockBean(name = "confirmationTokenRepository")
    ConfirmationTokenRepository confirmationTokenRepository;


    @Test
    void testCreateSeller() throws Exception {

        Mockito.when(javaMailSender.getHost()).thenReturn("qwertyuiop");
        Mockito.when(javaMailSender.getPort()).thenReturn(10000);
        Mockito.when(javaMailSender.getUsername()).thenReturn("qwertyui");
        Mockito.when(javaMailSender.getPassword()).thenReturn("qwertyui");

        //Given

        List addresses = new ArrayList();

        addresses.add(new JSONObject().put("addressLine", "Rohini")
                .put("city","New Delhi")
                .put("state","Delhi")
                .put("country","India")
                .put("zipCode","110027")
                .put("label","Home"));

        String jsonString = new JSONObject()
                .put("email", "siddharth.bhatia@tothenew.com")
                .put("firstName", "siddharth")
                .put("middleName", "K")
                .put("lastName", "testing")
                .put("username", "sidd")
                .put("password", "Abhi$9156")
                .put("confirmPassword", "Abhi$9156")
                .put("companyName", "TestingComp")
                .put("companyContact", "9897654321")
                .put("gstin", "18AABCT3518Q1ZV")
                /*.put("addresses", new JSONObject().put("addressLine", "Rohini")
                                                        .put("city","New Delhi")
                                                        .put("state","Delhi")
                                                        .put("country","India")
                                                        .put("zipCode","110027")
                                                        .put("label","Home"))*/
                .toString();

        System.out.println(jsonString);

        //When
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/sellerregistration").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON)
                //.header("","")
                .with(SecurityMockMvcRequestPostProcessors.user("abc"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON);


        //Then
        ResultActions resultAction =  mockMvc.perform(requestBuilder);
        MvcResult result = resultAction
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals("Registration Successful", result.getResponse().getContentAsString());

    }

   /* @Test
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

    }*/


}
