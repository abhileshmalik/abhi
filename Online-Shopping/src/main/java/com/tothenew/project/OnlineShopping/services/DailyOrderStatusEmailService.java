package com.tothenew.project.OnlineShopping.services;


import com.tothenew.project.OnlineShopping.entities.Seller;
import com.tothenew.project.OnlineShopping.orderprocessing.DailyOrderStatusEmail;
import com.tothenew.project.OnlineShopping.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import java.util.Iterator;

@Service
public class DailyOrderStatusEmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private MongoOperations mongoOperations;

    Logger logger = LoggerFactory.getLogger(DailyOrderStatusEmailService.class);

    public void saveEmailReport() {

        Iterable<Seller> sellers = userRepository.findSellerList();
        Iterator<Seller> sellerIterator = sellers.iterator();
        while (sellerIterator.hasNext()) {
            Seller seller = sellerIterator.next();
            String emailId = seller.getEmail();
            String subject = "Order Updates";
            String text = "Your order has been cancelled... " +
                            "Team Pro-Cart";
            emailSenderService.sendEmail(emailId, subject, text);

            DailyOrderStatusEmail dailyOrderStatusEmail = new DailyOrderStatusEmail();

            dailyOrderStatusEmail.setEmail(emailId);
            dailyOrderStatusEmail.setSubject(subject);
            dailyOrderStatusEmail.setMessage(text);

            mongoOperations.save(dailyOrderStatusEmail,"dailyOrderStatusEmail");

            logger.info("********** Email sent to all sellers **********");

        }
    }
}
