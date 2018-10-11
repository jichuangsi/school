/**
 * 
 */
package com.jichuangsi.microservice.common.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jichuangsi.microservice.common.model.UserInfoForToken;

/**
 * @author huangjiajun
 *
 */
public class TokenUtils {

	public static final Algorithm getTokenAlgorithmInstance(String secret) throws IllegalArgumentException, Exception {
		return Algorithm.HMAC512(secret);
	}

	public static final UserInfoForToken getUserInfoFromJwtToken(String jwtToken) {
		return getUserInfoFromJwtToken("userInfo");
	}

	public static final UserInfoForToken getUserInfoFromJwtToken(String jwtToken, String userClaim) {
		DecodedJWT jwt = JWT.decode(jwtToken);
		return JSONObject.parseObject(jwt.getClaim(userClaim).asString(), UserInfoForToken.class);
	}
}
