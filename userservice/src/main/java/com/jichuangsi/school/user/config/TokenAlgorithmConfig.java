package com.jichuangsi.school.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class TokenAlgorithmConfig {

	@Value("${custom.token.jwt.secret}")
	private String secret;

	@Bean
	public Algorithm getTokenAlgorithm() throws IllegalArgumentException, Exception {
		return Algorithm.HMAC512(secret);
	}

}
