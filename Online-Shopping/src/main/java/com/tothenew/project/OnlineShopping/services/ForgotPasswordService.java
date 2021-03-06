package com.tothenew.project.OnlineShopping.services;

import com.tothenew.project.OnlineShopping.exception.ValidationException;
import com.tothenew.project.OnlineShopping.model.ForgotPasswordModel;
import com.tothenew.project.OnlineShopping.entities.User;
import com.tothenew.project.OnlineShopping.exception.TokenExpiredException;
import com.tothenew.project.OnlineShopping.exception.UserNotFoundException;
import com.tothenew.project.OnlineShopping.repos.ResetPasswordRepository;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.tokens.ResetPasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String resetUserPassword(String email){

        User user=userRepository.findByEmailIgnoreCase(email);

        if(user == null)
            throw new UserNotFoundException("User not found");

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken = resetPasswordRepository.findByUser(user);
        if(resetPasswordToken!=null) {
            resetPasswordToken.calculateToken();
            resetPasswordRepository.save(resetPasswordToken);
        }
        else{
            resetPasswordToken = new ResetPasswordToken(user);
            resetPasswordRepository.save(resetPasswordToken);
        }

        String emailId = user.getEmail();
        String subject = "Reset your password";
        String text = "To reset your password , please click here "
                +"http://localhost:8080/reset-password?token="+resetPasswordToken.getToken();
        emailSenderService.sendEmail(emailId,subject,text);

        logger.info("********** Password Reset link sent to the user **********");

        return "A link has been sent to your email for password reset.";
    }

    public String updatePassword(String resetToken, ForgotPasswordModel forgotPasswordModel){
        ResetPasswordToken resetPasswordToken= resetPasswordRepository.findByToken(resetToken);

        if(resetPasswordToken==null) {
            throw new TokenExpiredException("Invalid Token");
        }
        Date presentDate = new Date();

        if (resetPasswordToken.getExpiryDate().getTime() - presentDate.getTime() <= 0){
            throw new TokenExpiredException("Token has been expired, request for new Token via Forgot Password Link");
        }
        else {
            User user = userRepository.findByEmailIgnoreCase(resetPasswordToken.getUser().getEmail());

            if(forgotPasswordModel.getPassword().matches(forgotPasswordModel.getConfirmPassword())) {

                String pass = forgotPasswordModel.getPassword();
                user.setPassword(passwordEncoder.encode(pass));
                user.setEnabled(true);
                user.setNonLocked(true);
                userRepository.save(user);
                String emailId = user.getEmail();
                String subject = "Password Updated !!";
                String text = "Your password has been changed successfully!!";
                emailSenderService.sendEmail(emailId, subject, text);

                logger.info("********** User Password Updated **********");

                return "Password updated successfully!!!";

            }
            else {
                throw new ValidationException("password and confirm password not matched !");
            }
        }
    }


}
