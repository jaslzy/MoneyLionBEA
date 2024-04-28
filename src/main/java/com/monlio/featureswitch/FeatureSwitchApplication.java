package com.monlio.featureswitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication //@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // Jasper temporary - since currently "no {instead of, got}" data source.
public class FeatureSwitchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeatureSwitchApplication.class, args);
	}

}
