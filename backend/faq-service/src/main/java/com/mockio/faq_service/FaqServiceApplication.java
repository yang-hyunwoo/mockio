package com.mockio.faq_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mockio")
public class FaqServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaqServiceApplication.class, args);
	}

}
