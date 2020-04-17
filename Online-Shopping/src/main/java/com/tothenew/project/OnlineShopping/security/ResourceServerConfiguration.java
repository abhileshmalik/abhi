package com.tothenew.project.OnlineShopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/category").permitAll()
                .antMatchers("/productcategories").permitAll()

                .antMatchers("/products").permitAll()
                .antMatchers("/products/{category_name}").permitAll()
                .antMatchers("/product/{product_name}").permitAll()
                .antMatchers("/confirm").permitAll()
                .antMatchers("/confirm-account").permitAll()
                .antMatchers("/forgot-password").permitAll()
                .antMatchers("/reset-password").permitAll()
                .antMatchers("/account-unlock/{username}").permitAll()
                .antMatchers("/do-unlock").permitAll()

                .antMatchers("/registration").anonymous()
                .antMatchers("/customerregistration").anonymous()
                .antMatchers("/sellerregistration").anonymous()

                .antMatchers("/seller/home").hasAnyRole("SELLER")
                .antMatchers("/seller/home/profile").hasAnyRole("SELLER")
                .antMatchers("/seller/home/profile/address").hasAnyRole("SELLER")
                .antMatchers("/seller/updateProfile").hasAnyRole("SELLER")
                .antMatchers("/seller/updateProfile/address{address_id}").hasAnyRole("SELLER")
                .antMatchers("/save-product/category/{category_name}").hasAnyRole("SELLER")

                .antMatchers("/customer/home").hasAnyRole("USER")
                .antMatchers("/customer/home/profile").hasAnyRole("USER")
                .antMatchers("/customer/home/profile/address").hasAnyRole("USER")
                .antMatchers("/add-to-cart/{productVariation_id}").hasAnyRole("USER")
                .antMatchers("/order/{cart_id}").hasAnyRole("USER")
                .antMatchers("/addreview/{product_id}").hasAnyRole("USER")

                .antMatchers("/product/{product_id}").hasAnyRole("USER")
                .antMatchers("/product/variant/{vid}").hasAnyRole("USER")

                .antMatchers("/admin/home").hasAnyRole("ADMIN")
                .antMatchers("/customers").hasAnyRole("ADMIN")
                .antMatchers("/sellers").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateuser/{uid}").hasAnyRole("ADMIN")
                .antMatchers("/admin/deactivateuser/{uid}").hasAnyRole("ADMIN")
                .antMatchers("/add-category").hasAnyRole("ADMIN")
                .antMatchers("/add-category/{parentCategory}").hasAnyRole("ADMIN")



                .antMatchers("/doLogout").hasAnyRole("ADMIN","SELLER","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}