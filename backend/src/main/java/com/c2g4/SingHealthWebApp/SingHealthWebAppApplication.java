package com.c2g4.SingHealthWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class SingHealthWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingHealthWebAppApplication.class, args);
		
		
	}

}
