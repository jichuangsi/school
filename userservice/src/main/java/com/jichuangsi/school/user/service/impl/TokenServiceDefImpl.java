/**
 * 
 */
package com.jichuangsi.school.user.service.impl;

import javax.annotation.Resource;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.service.ITokenService;

import java.util.Date;

/**
 * @author huangjiajun
 *
 */
@Service
public class TokenServiceDefImpl implements ITokenService {
	@Resource
	private Algorithm algorithm;
	@Value("${com.jichuangsi.school.token.userClaim}")
	private String userClaim;

	@Override
	public String getToken(String userId) {
		// 获取用户信息
		UserInfoForToken userInfoForToken = new UserInfoForToken("123", "456","张三","777",String.valueOf(new Date().getTime()));

		String userJson = JSON.toJSONString(userInfoForToken);
		return JWT.create().withClaim(userClaim, userJson).sign(algorithm);
	}

	@Override
	public boolean checkToken(String token) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		try {
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			e.printStackTrace();
			return false;
		}

	}

}
