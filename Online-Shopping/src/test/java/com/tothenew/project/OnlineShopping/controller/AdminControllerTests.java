package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.model.CustomerUpdateModel;
import com.tothenew.project.OnlineShopping.services.AdminDaoService;
import com.tothenew.project.OnlineShopping.services.ProductDaoService;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdminControllerTests {

    UserDaoService userDaoService = new UserDaoService();
    AdminDaoService adminDaoService = new AdminDaoService();
    ProductDaoService productDaoService= new ProductDaoService();


    @Test
    void testEnableSellerAccount() {

        //Given
        Long sellerId = 4l;

        String expected = "Seller Account Enabled successfully";

        //When
        String actual = userDaoService.enableSellerAccount(sellerId);

        //Then
        assertEquals(expected,actual,"Seller Enabled");

    }

    @Test
    void testActivateUser() {

        //Given
        Long id = 10l;

        String expected = "User Activated";

        //When
        String actual = adminDaoService.activateUser(id);

        //Then
        assertEquals(expected,actual,"User Activated");

    }

    @Test
    void testDeactivateUser() {

        //Given
        Long id = 10l;

        String expected = "User Deactivated";

        //When
        String actual = adminDaoService.deactivateUser(id);

        //Then
        assertEquals(expected,actual,"User Deactivated");
    }

    //For User Activation
    @Test
    void should_ThrowsUserNotFoundException_when_InvalidUserIdEntered() {

        //Given
        Long id = 101l;

        //When
        Executable executable = () -> adminDaoService.activateUser(id);

        //Then
        assertThrows(UserNotFoundException.class, executable);
    }

    //For User Deactivation
    @Test
    void should_ThrowUserNotFoundException_when_InvalidUserIdEntered() {

        //Given
        Long id = 101l;

        //When
        Executable executable = () -> adminDaoService.deactivateUser(id);

        //Then
        assertThrows(UserNotFoundException.class, executable);

    }


    @Test
    void testActivateProduct() {

        //Given
        Long pid = 6l;

        String expected = "Product Activated";

        //When
        String actual = productDaoService.activateProduct(pid);

        //Then
        assertEquals(expected,actual,"Product Activated");
    }

    @Test
    void testDeactivateProduct() {

        //Given
        Long pid = 6l;

        String expected = "Product Deactivated";

        //When
        String actual = productDaoService.deactivateProduct(pid);

        //Then
        assertEquals(expected,actual,"Product Deactivated");
    }





}
