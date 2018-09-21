package com.example.gateway.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gateway.constant.ResultCode;
import com.example.gateway.model.ResponseModel;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
	protected final Log log = LogFactory.getLog(this.getClass());

	@RequestMapping("/hystrixTimeout")
	public Mono<ResponseModel> hystrixTimeout() {
		log.error("触发了断路由");
		return Mono.just(new ResponseModel(ResultCode.SYS_BUSY, ResultCode.SYS_BUSY_MSG));
	}

}
