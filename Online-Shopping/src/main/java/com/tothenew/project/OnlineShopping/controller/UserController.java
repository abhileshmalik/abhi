package com.tothenew.project.OnlineShopping.controller;

import com.tothenew.project.OnlineShopping.entities.AppUser;
import com.tothenew.project.OnlineShopping.entities.Customer;
import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private TokenStore tokenStore;

    AppUser appUser;


    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/customers")
    public List<Customer> retrieveAllCustomers() {
        return userDaoService.findAllCustomers();
    }

    @GetMapping("/sellers")
    public List<Seller> retrieveAllSellers() {
        return userDaoService.findAllSellers();
    }


    /*   @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = userDaoService.saveNewUser(user);

        URI location =  ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(savedUser.getUser_id())
                .toUri();

        return ResponseEntity.created(location).build();
    }*/

    @PostMapping(path = "/registration")
    public void createUser(@RequestBody User user) {
        User newUser = userDaoService.saveNewUser(user);
    }

    @PostMapping(path = "/customerregistration")
    public String createCustomer(@RequestBody Customer customer) {
        String message = userDaoService.saveNewCustomer(customer);
        return message;
    }

    @PostMapping(path = "/sellerregistration")
    public String createSeller(@RequestBody Seller seller) {
        String message = userDaoService.saveNewSeller(seller);
        return message;
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        String message= userDaoService.confirmUserAccount(confirmationToken);
        return message;
    }

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

    @GetMapping("/")
    public String index(){
        return "Welcome To Online Shopping Portal";
    }

    @GetMapping("/admin/home")
    public String adminHome(){
        return "Welcome Admin To Online Shopping Portal";
    }

    @GetMapping("/seller/home")
    public String sellerHome(){
        //System.out.println(appUser.getUid());
        return "Welcome Seller To Online Shopping Portal";
    }

    @GetMapping("/customer/home")
    public Customer userHome(){
        Customer customer = userDaoService.getLoggedInCustomer();

        return customer;

        /*return "Welcome Customer To Online Shopping Portal";*/
    }



}
