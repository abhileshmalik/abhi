package com.tothenew.project.OnlineShopping.scheduler;

import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import com.tothenew.project.OnlineShopping.services.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@EnableAsync(proxyTargetClass = true)
@Component
public class EmailScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(EmailScheduler.class);


    @Scheduled(cron = "24 13 2 * * ?", zone = "Indian/Maldives")
    //@Scheduled(initialDelay = 1000,fixedDelay = 10000)
    public void run(){
        Iterable<Seller> sellers = userRepository.findSellerList();
        Iterator<Seller> sellerIterator = sellers.iterator();
        while (sellerIterator.hasNext()) {
            Seller seller = sellerIterator.next();
            String emailId = seller.getEmail();
            String subject = "Order Updates";
            String text = "Your order has been cancelled...";
            emailSenderService.sendEmail(emailId, subject, text);

            logger.info("********** Email sent to all sellers **********");
        }
    }
}
