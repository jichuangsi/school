package com.jichuangsi.school.user.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.school.user.model.ResponseModel;
import com.jichuangsi.school.user.service.ITokenService;

@RestController
public class TokenController {

	@Resource
	private ITokenService tokenService;
	
	@GetMapping("/getToken")
	public ResponseModel<String> getToken(@RequestBody String userId) {
		return ResponseModel.sucess("", tokenService.getToken(userId));
	}
}
