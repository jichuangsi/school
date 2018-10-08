/**
 * 
 */
package com.jichuangsi.school.user.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.UserInfoForToken;
import com.jichuangsi.school.user.service.ITokenService;

/**
 * @author huangjiajun
 *
 */
@Service
public class TokenServiceDefImpl implements ITokenService {
	@Resource
	private Algorithm algorithm;
	@Value("${custom.token.jwt.userClaim}")
	private String userClaim;

	@Override
	public String getToken(String userId) {
		// 获取用户信息
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId("123");
		userInfo.setUserName("张三");

		String userJson = JSON.toJSONString(UserInfoForToken.getFromUserInfo(userInfo));
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
