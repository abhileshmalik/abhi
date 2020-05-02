package com.tothenew.project.OnlineShopping.entities;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@ApiModel(description = "Additional Seller Information other than common user information")
public class Seller extends User {

    private String companyName;
    private String companyContact;
    private String gstin;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

}
