package com.jichuangsi.school.classinteraction.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class TokenAlgorithmConfig {

	@Value("${app.token.publicKey}")
	private String publicKey;

	@Bean
	public Algorithm getTokenAlgorithm() {
		return Algorithm.HMAC256(publicKey);
	}

}
