package com.tripto.booking_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration
public class BookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingAppApplication.class, args);
	}

}
