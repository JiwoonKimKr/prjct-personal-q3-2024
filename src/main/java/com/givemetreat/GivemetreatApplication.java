package com.givemetreat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GivemetreatApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GivemetreatApplication.class, args);
	}

}
