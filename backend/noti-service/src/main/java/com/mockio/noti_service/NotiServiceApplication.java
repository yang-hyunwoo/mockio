package com.mockio.noti_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mockio")
public class NotiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotiServiceApplication.class, args);
	}

}
