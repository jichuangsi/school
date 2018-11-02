package com.jichuangsi.school.gateway.filter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.school.gateway.model.ResponseModel;

import reactor.core.publisher.Mono;

@Component
public class TokenCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	private Log log = LogFactory.getLog(TokenCheckGatewayFilterFactory.class);

	@Value("${app.token.headerName}")
	private String headerName;

	@Value("${app.token.cache.expireAfterAccessWithMinutes}")
	private long expireAfterAccessWithMinutes;

	@Value("${app.token.ingoreTokenUrls}")
	private String[] ingoreTokenUrls;

	@Autowired
	private Algorithm tokenAlgorithm;

	@Override
	public final GatewayFilter apply(Object config) {

		return (exchange, chain) -> {
			try {
				final ServerHttpRequest request = exchange.getRequest();

				final String url = request.getURI().getPath();
				if (null != ingoreTokenUrls && ingoreTokenUrls.length > 0) {
					for (String ingoreUrl : ingoreTokenUrls) {
						if (ingoreUrl.equals(url) || url.startsWith(ingoreUrl)) {// 对免检查token的url放行
							return chain.filter(exchange);
						}
					}
				}

				final String accessToken = request.getHeaders().getFirst(headerName);
				if (!StringUtils.isEmpty(accessToken)) {
					final JWTVerifier verifier = JWT.require(tokenAlgorithm).build();
					verifier.verify(accessToken);// 校验有效性
					// todo 校验有效期
					return chain.filter(exchange);
				} else {
					return buildResponse(exchange, ResultCode.TOKEN_MISS, ResultCode.TOKEN_MISS_MSG);
				}
			} catch (JWTVerificationException e) {
				log.error("token检验不通过：" + e.getMessage());
				return buildResponse(exchange, ResultCode.TOKEN_CHECK_ERR, ResultCode.TOKEN_CHECK_ERR_MSG);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return buildResponse(exchange, ResultCode.SYS_ERROR, ResultCode.SYS_ERROR_MSG);
			}

		};
	}

	private final Mono<Void> buildResponse(ServerWebExchange exchange, String code, String msg) {
		ServerHttpResponse response = exchange.getResponse();
		// 设置headers
		HttpHeaders httpHeaders = response.getHeaders();
		httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
		httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

		DataBuffer bodyDataBuffer;
		try {
			bodyDataBuffer = response.bufferFactory()
					.wrap(JSONObject.toJSONString(new ResponseModel(code, msg)).getBytes("UTF-8"));
			return response.writeWith(Mono.just(bodyDataBuffer));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			bodyDataBuffer = response.bufferFactory()
					.wrap(JSONObject.toJSONString(new ResponseModel(code, msg)).getBytes());
			return response.writeWith(Mono.just(bodyDataBuffer));
		}
	}

}
