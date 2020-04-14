package com.tothenew.project.OnlineShopping.entities;

import com.tothenew.project.OnlineShopping.security.GrantAuthorityImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

public class AppUser implements UserDetails {

    private Long uid;
    private String name;
    private String username;
    private String password;
    private Boolean isEnabled;
    private Boolean isNonLocked;
    List<GrantAuthorityImpl> grantAuthorities;

    public AppUser(Long uid, String name, String username, String password, Boolean isEnabled, Boolean isNonLocked, List<GrantAuthorityImpl> grantAuthorities) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.isNonLocked = isNonLocked;
        this.grantAuthorities = grantAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


    public Long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}