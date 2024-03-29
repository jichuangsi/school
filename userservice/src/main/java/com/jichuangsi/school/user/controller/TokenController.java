package com.jichuangsi.school.user.controller;

import javax.annotation.Resource;

import com.jichuangsi.microservice.common.model.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.school.user.service.ITokenService;

@RestController
public class TokenController {

	@Resource
	private ITokenService tokenService;
	
	/**
	 * 根据用户ID获取token
	 *
	 */
	@GetMapping("/getToken/{userId}")
	public ResponseModel<String> getToken(@PathVariable String userId) {
		return ResponseModel.sucess("", tokenService.getToken(userId));
	}
}
