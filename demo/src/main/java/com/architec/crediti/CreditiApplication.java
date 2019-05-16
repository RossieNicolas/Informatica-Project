package com.architec.crediti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CreditiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditiApplication.class, args);
	}
}
