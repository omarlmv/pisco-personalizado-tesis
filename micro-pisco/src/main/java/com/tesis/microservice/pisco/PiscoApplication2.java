package com.tesis.microservice.pisco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableAutoConfiguration
//@EnableDiscoveryClient
public class PiscoApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(PiscoApplication2.class, args);
	}
}