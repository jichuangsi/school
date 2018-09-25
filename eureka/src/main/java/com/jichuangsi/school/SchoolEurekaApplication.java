package com.jichuangsi.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SchoolEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolEurekaApplication.class, args);
	}
}
