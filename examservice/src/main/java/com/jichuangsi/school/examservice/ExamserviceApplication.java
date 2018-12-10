package com.jichuangsi.school.examservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ExamserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamserviceApplication.class, args);
	}
}
