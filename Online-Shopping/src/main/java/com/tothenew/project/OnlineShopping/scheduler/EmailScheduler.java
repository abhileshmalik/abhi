package com.tothenew.project.OnlineShopping.scheduler;

import com.tothenew.project.OnlineShopping.services.DailyOrderStatusEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync(proxyTargetClass = true)
@Component
public class EmailScheduler {


    @Autowired
    private DailyOrderStatusEmailService dailyOrderStatusEmailService;


    @Scheduled(cron = "25 11 * * * ?")             // It works on UTC Time Zone by default
   // @Scheduled(initialDelay = 1000,fixedDelay = 5000)
    ///@Scheduled(cron = "51 3 * * * ?", zone = "Indian/Maldives")
    public void run(){

        dailyOrderStatusEmailService.saveEmailReport();

    }
}
