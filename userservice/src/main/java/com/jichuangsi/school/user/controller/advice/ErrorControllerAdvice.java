package com.jichuangsi.school.user.controller.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jichuangsi.school.user.model.ResponseModel;

@RestControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler
	public ResponseModel<Object> handler(Exception e) {
		return ResponseModel.fail("", e.getMessage());
	}

}
