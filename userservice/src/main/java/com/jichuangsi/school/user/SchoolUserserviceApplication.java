package com.jichuangsi.school.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class SchoolUserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolUserserviceApplication.class, args);
	}
}
