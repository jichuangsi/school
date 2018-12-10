package com.jichuangsi.school.eaxmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EaxmApplication {

	public static void main(String[] args) {
		SpringApplication.run(EaxmApplication.class, args);
	}
}
