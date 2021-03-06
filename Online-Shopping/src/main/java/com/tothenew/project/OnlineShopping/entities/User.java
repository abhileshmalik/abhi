package com.tothenew.project.OnlineShopping.entities;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonFilter("userfilter")
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "Common details about the Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long user_id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isDeleted;
    private Boolean isActive;
    private Boolean isNonLocked;
    private Boolean isEnabled;
    private String role;
    private Long profileImageId;

    @JsonIgnore
    private Integer attempts=0;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Address> addresses;

    @Column
    @CreatedDate
    private Date createdDate;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getNonLocked() {
        return isNonLocked;
    }

    public void setNonLocked(Boolean locked) {
        isNonLocked = locked;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Long getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Long profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void addAddresses(Address address) {
        if (address !=null) {
            if (addresses==null) {
                addresses = new HashSet<>();
            }
            address.setUser(this);
            addresses.add(address);
        }
    }

}
