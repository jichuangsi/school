package com.jichuangsi.school.courseservice.controller.advice;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class CommonControllerAdvice {

	@ModelAttribute
	public void translateHeader(@RequestHeader @Nullable String userInfo, @RequestHeader @Nullable String accessToken,
			Model model) throws UnsupportedEncodingException {

		if (!StringUtils.isEmpty(accessToken)) {
			DecodedJWT jwt = JWT.decode(accessToken);
			String user = jwt.getClaim("userInfo").asString();
			UserInfoForToken test = JSONObject.parseObject(user,UserInfoForToken.class);
			model.addAttribute("userInfo", JSONObject.parseObject(user,UserInfoForToken.class));
		}
	}

	@ExceptionHandler
	public ResponseModel<Object> handler(Exception e) {
		return ResponseModel.fail("", e.getMessage());

	}

}
