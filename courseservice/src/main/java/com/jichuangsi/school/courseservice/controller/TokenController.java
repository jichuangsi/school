package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.service.ITokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TokenController {

	@Resource
	private ITokenService tokenService;
	
	@GetMapping("/getToken/{userId}")
	public ResponseModel<String> getToken(@PathVariable String userId) {
		return ResponseModel.sucess("", tokenService.getToken(userId));
	}

	@PostMapping("/returnException")
	public ResponseModel<String> returnException() throws Exception{
		throw new Exception("test exception");
	}
}
