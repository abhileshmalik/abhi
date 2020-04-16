package com.tothenew.project.OnlineShopping.dto;

import com.tothenew.project.OnlineShopping.entities.Address;
import com.tothenew.project.OnlineShopping.validations.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@PasswordMatches
public class SellerRegisterDto {

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(max = 1, message = "Only one address is required")
    private Set<Address> addresses;

    @NotNull
    @Password
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    @Column(unique = true)
    @GST
    private String gst;

    @NotNull
    @Column(unique = true)
    private String company_name;

    @Phone
    private String company_contact;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_contact() {
        return company_contact;
    }

    public void setCompany_contact(String company_contact) {
        this.company_contact = company_contact;
    }
}
