package com.datngo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherBeApplication.class, args);
	}

}
