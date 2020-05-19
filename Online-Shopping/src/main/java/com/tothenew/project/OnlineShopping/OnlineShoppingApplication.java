package com.tothenew.project.OnlineShopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import java.util.Locale;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class OnlineShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShoppingApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver(){
		AcceptHeaderLocaleResolver localeResolver=new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}


}
