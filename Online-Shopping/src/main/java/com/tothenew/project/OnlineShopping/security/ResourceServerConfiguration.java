package com.tothenew.project.OnlineShopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
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
@EnableJpaAuditing
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
                .antMatchers("/allcategories").permitAll()
                .antMatchers("/productcategories").permitAll()

                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()

                .antMatchers("/confirm").permitAll()
                .antMatchers("/confirm-account").permitAll()
                .antMatchers("/resendactToken").permitAll()
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
                .antMatchers("/seller/updateProfile/address/{address_id}").hasAnyRole("SELLER")
                .antMatchers("/seller/updatePassword").hasAnyRole("SELLER")
                .antMatchers("/seller/updateproduct/{pid}").hasAnyRole("SELLER")
                .antMatchers("/seller/updateproduct/variant/{vid}").hasAnyRole("SELLER")
                .antMatchers("/seller/deleteproduct/{pid}").hasAnyRole("SELLER")
                .antMatchers("/seller/products").hasAnyRole("SELLER")
                .antMatchers("/save-product/category/{category_name}").hasAnyRole("SELLER")

                .antMatchers("/customer/home").hasAnyRole("USER")
                .antMatchers("/customer/uploadImage").hasAnyRole("USER")
                .antMatchers("/customer/home/profile").hasAnyRole("USER")
                .antMatchers("/customer/home/profile/address").hasAnyRole("USER")
                .antMatchers("/customer/updateProfile").hasAnyRole("USER")
                .antMatchers("/customer/updatePassword").hasAnyRole("USER")
                .antMatchers("/customer/addAddress").hasAnyRole("USER")
                .antMatchers("/customer/deleteAddress/{address_id}").hasAnyRole("USER")
                .antMatchers("/customer/updateAddress/{address_id}").hasAnyRole("USER")
                .antMatchers("/add-to-cart/{productVariation_id}").hasAnyRole("USER")
                .antMatchers("/order/{cart_id}").hasAnyRole("USER")
                .antMatchers("/addreview/{product_id}").hasAnyRole("USER")

                .antMatchers("/product/{product_id}").hasAnyRole("USER","ADMIN")
                .antMatchers("/product/variant/{vid}").hasAnyRole("USER","ADMIN")
                .antMatchers("/products/{category_name}").hasAnyRole("USER","ADMIN")

                .antMatchers("/admin/home").hasAnyRole("ADMIN")
                .antMatchers("/customers").hasAnyRole("ADMIN")
                .antMatchers("/sellers").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateuser/{uid}").hasAnyRole("ADMIN")
                .antMatchers("/admin/deactivateuser/{uid}").hasAnyRole("ADMIN")
                .antMatchers("/admin/enableSeller/{sellerId}").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateproduct/{pid}").hasAnyRole("ADMIN")
                .antMatchers("/admin/deactivateproduct/{pid}").hasAnyRole("ADMIN")
                .antMatchers("/metadata-fields/add").hasAnyRole("ADMIN")
                .antMatchers("/allmetadatafields").hasAnyRole("ADMIN")
                .antMatchers("/metadata-fields/addValues/{categoryId}/{metaFieldId}")
                .hasAnyRole("ADMIN")

                .antMatchers("/add-category").hasAnyRole("ADMIN")
                .antMatchers("/add-category/{parentCategory}").hasAnyRole("ADMIN")
                .antMatchers("/updateCategory/{category}").hasAnyRole("ADMIN")


                .antMatchers("/doLogout").hasAnyRole("ADMIN","SELLER","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}