package com.jichuangsi.microservice.common.advice;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
//import com.example.webflux.model.ResponseModel;
//import com.example.webflux.model.UserInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfo;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class MyControllerAdvice {

	@ModelAttribute
	public void translateHeader(@RequestHeader @Nullable String userInfo, @RequestHeader @Nullable String accessToken,
			Model model) throws UnsupportedEncodingException {

//		if (!StringUtils.isEmpty(userInfo)) {
//			UserInfo info = JSONObject.parseObject(URLDecoder.decode(userInfo, "UTF-8"), UserInfo.class);
//			model.addAttribute("userInfo", info);
//		}
		System.out.println("===========lalal==============");
		if (!StringUtils.isEmpty(accessToken)) {
			DecodedJWT jwt = JWT.decode(accessToken);
			model.addAttribute("userInfo", JSONObject.parseObject(jwt.getClaim("userInfo").asString(),UserInfo.class));
		}
	}

	@ExceptionHandler
	public ResponseModel<Object> handler(Exception e) {
		return ResponseModel.fail("", e.getMessage());

	}

}
