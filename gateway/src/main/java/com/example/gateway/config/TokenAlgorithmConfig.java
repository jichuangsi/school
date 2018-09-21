package com.example.gateway.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.example.gateway.util.RSAUtils;

@Configuration
public class TokenAlgorithmConfig {

	private Log log = LogFactory.getLog(TokenAlgorithmConfig.class);

	@Value("${app.token.publicKey}")
	private String publicKeyB64;

	@Bean
	public Algorithm getTokenAlgorithm() {

		return Algorithm.RSA512(new RSAKeyProvider() {

			@Override
			public RSAPublicKey getPublicKeyById(String keyId) {
				try {
					return RSAUtils.getPublicKey(publicKeyB64);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					throw new RuntimeException(e.getMessage());
				}
			}

			@Override
			public String getPrivateKeyId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RSAPrivateKey getPrivateKey() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

}
