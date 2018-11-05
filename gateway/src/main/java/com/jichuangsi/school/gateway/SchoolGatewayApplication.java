package com.jichuangsi.school.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@SpringBootApplication
public class SchoolGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolGatewayApplication.class, args);
	}
}
