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
                .antMatchers("/add-category/{parentCategory}").permitAll()
                .antMatchers("/products").permitAll()
                .antMatchers("/products/{category_name}").permitAll()
                .antMatchers("/product/{product_name}").permitAll()
                .antMatchers("/product/{product_id}").permitAll()
                .antMatchers("/product/variant/{vid}").permitAll()
                .antMatchers("/confirm").permitAll()
                .antMatchers("/confirm-account").permitAll()
                .antMatchers("/forgot-password").permitAll()
                .antMatchers("/reset-password").permitAll()
                .antMatchers("/registration").anonymous()
                .antMatchers("/customerregistration").anonymous()
                .antMatchers("/sellerregistration").anonymous()
                .antMatchers("/admin/home").hasAnyRole("ADMIN")
                .antMatchers("/seller/home").hasAnyRole("SELLER")
                .antMatchers("/customer/home").hasAnyRole("USER")
                .antMatchers("/customers").hasAnyRole("ADMIN")
                .antMatchers("/sellers").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateuser/{uid}").permitAll()
                .antMatchers("/admin/deactivateuser/{uid}").permitAll()
                .antMatchers("/add-category").permitAll()
                .antMatchers("/{seller_user_id}/save-product/category/{category_name}")
                .permitAll()

                .antMatchers("/{customer_user_id}/add-to-cart/{productVariation_id}").permitAll()
                .antMatchers("/{customer_user_id}/order/{cart_id}").permitAll()
                .antMatchers("/doLogout").hasAnyRole("ADMIN","SELLER","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}