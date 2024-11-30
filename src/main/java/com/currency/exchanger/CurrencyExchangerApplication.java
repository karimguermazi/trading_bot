package com.currency.exchanger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableScheduling
public class CurrencyExchangerApplication {

	public static void main(String[] args) {
    SpringApplication.run(CurrencyExchangerApplication.class, args);
	}

}
