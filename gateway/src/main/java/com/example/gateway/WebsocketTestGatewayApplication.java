package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@SpringBootApplication
public class WebsocketTestGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketTestGatewayApplication.class, args);
	}
}
