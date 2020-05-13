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
                .antMatchers("/view-variant/{vid}").permitAll()

                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
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

                .antMatchers("/seller/**").hasAnyRole("SELLER")
                .antMatchers("/save-product/category/{category_name}").hasAnyRole("SELLER")
                .antMatchers("/save-productVariation/{product_id}").hasAnyRole("SELLER")
                .antMatchers("/product/uploadImage/{pid}").hasAnyRole("SELLER")
                .antMatchers("/product/variant/uploadImage/{vid}").hasAnyRole("SELLER")

                .antMatchers("/profile/uploadImage}").hasAnyRole("SELLER", "USER")
                .antMatchers("/download/ProfileImage").hasAnyRole("SELLER", "USER")

                .antMatchers("/customer/**").hasAnyRole("USER")

                .antMatchers("/add-to-cart/{productVariation_id}").hasAnyRole("USER")
                .antMatchers("/order/{cart_id}").hasAnyRole("USER")
                .antMatchers("/addreview/{product_id}").hasAnyRole("USER")
                .antMatchers("/subcategies/{categoryid}").hasAnyRole("USER")

                .antMatchers("/product/{product_id}").hasAnyRole("USER","ADMIN")
                .antMatchers("/product/variant/{vid}").hasAnyRole("USER","ADMIN")
                .antMatchers("/products/{category_name}").hasAnyRole("USER","ADMIN")
                .antMatchers("/similar-products/products/{pid}").hasAnyRole("USER","ADMIN")

                .antMatchers("/admin/**").hasAnyRole("ADMIN")

                .antMatchers("/customers").hasAnyRole("ADMIN")
                .antMatchers("/sellers").hasAnyRole("ADMIN")
                .antMatchers("/metadata-fields/add").hasAnyRole("ADMIN")
                .antMatchers("/metadata-fields/addValues/{categoryId}/{metaFieldId}")
                .hasAnyRole("ADMIN")

                .antMatchers("/add-category").hasAnyRole("ADMIN")
                .antMatchers("/add-category/{parentCategory}").hasAnyRole("ADMIN")
                .antMatchers("/updateCategory/{category}").hasAnyRole("ADMIN")

                .antMatchers("/allmetadatafields").hasAnyRole("ADMIN","SELLER")
                .antMatchers("/allmetadatafieldValues").hasAnyRole("ADMIN","SELLER")

                .antMatchers("/download/product/variation/image/{vid}").hasAnyRole("SELLER", "USER","ADMIN")
                .antMatchers("/download/product/image/{pid}").hasAnyRole("SELLER", "USER","ADMIN")

                .antMatchers("/doLogout").hasAnyRole("ADMIN","SELLER","USER")

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}