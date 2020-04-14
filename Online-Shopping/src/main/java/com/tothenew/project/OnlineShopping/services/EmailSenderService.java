package com.tothenew.project.OnlineShopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Async
    public void sendEmail(String emailId, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailId);
        msg.setSubject(subject);
        msg.setText(text);

        javaMailSender.send(msg);

    }

}
