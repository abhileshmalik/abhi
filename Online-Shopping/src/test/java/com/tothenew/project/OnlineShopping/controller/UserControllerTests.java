package com.tothenew.project.OnlineShopping.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tothenew.project.OnlineShopping.controller.UserController;
import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.exception.ResourceNotFoundException;
import com.tothenew.project.OnlineShopping.exception.ValidationException;
import com.tothenew.project.OnlineShopping.model.*;
import com.tothenew.project.OnlineShopping.repos.AddressRepository;
import com.tothenew.project.OnlineShopping.repos.ConfirmationTokenRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.services.EmailSenderService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, UserDaoService.class, EmailSenderService.class})
@WebMvcTest(UserController.class)
public class UserControllerTests {

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
    void testHomepage() throws Exception {
        //TODO call api to get token

        //TODO use received token to call further API

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/")
                //.header("","")
                .with(SecurityMockMvcRequestPostProcessors.user("abc"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON);

        ResultActions resultAction =  mockMvc.perform(requestBuilder);

        MvcResult result = resultAction
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("Welcome To The Pro-Cart", result.getResponse().getContentAsString());
    }


    @Test
    void testCreateCustomer() throws Exception {

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
                .put("email", "abhileshmalik03@gmail.com")
                .put("firstName", "abhi")
                .put("middleName", "")
                .put("lastName", "testing")
                .put("username", "abhi03")
                .put("password", "Abhi$9156")
                .put("confirmPassword", "Abhi$9156")
                .put("contact", "9897654321")
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
                .post("/customerregistration").content(jsonString)
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
        assertEquals("Registration Successful, Please verify your account via Activation link sent on your registered email",
                result.getResponse().getContentAsString());

    }

    /* @Test
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
    }*/






}
