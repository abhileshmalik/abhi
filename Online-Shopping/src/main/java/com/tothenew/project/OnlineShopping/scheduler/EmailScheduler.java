package com.tothenew.project.OnlineShopping.scheduler;

import com.tothenew.project.OnlineShopping.services.DailyOrderStatusEmailService;
import com.tothenew.project.OnlineShopping.services.ProductVariationDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync(proxyTargetClass = true)
@Component
public class EmailScheduler {


    @Autowired
    private DailyOrderStatusEmailService dailyOrderStatusEmailService;

    @Autowired
    private ProductVariationDaoService productVariationDaoService;

    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(initialDelay = 1000,fixedDelay = 5000)
    public void run(){

        // Task 1 (Sending daily Order Update Emails to seller)
        dailyOrderStatusEmailService.saveEmailReport();

        // Task 2 (Updating ProductVariant quantity from RedisDb to MySQL Db)
        productVariationDaoService.autoUpdateVariationQuantity();

    }
}
