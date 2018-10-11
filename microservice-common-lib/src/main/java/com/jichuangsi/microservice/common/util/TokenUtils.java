/**
 * 
 */
package com.jichuangsi.microservice.common.util;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author huangjiajun
 *
 */
public class TokenUtils {
	public static Algorithm getTokenAlgorithmInstance(String secret) throws IllegalArgumentException, Exception {
		return Algorithm.HMAC512(secret);
	}
}
