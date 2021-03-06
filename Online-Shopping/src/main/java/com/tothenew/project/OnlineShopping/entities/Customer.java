package com.tothenew.project.OnlineShopping.entities;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@ApiModel(description = "Additional Customer Information other than common user information")
public class Customer extends User{

    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
