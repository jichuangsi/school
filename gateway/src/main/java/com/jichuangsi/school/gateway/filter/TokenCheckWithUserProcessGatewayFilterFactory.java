//在网关检查token，同时转换jwt用户数据并缓存--考虑到网关转换效率和内存问题，暂不适用
package com.jichuangsi.school.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.school.gateway.model.ResponseModel;

import reactor.core.publisher.Mono;

@Component
public class TokenCheckWithUserProcessGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${app.token.headerName}")
	private String headerName;

	@Value("${app.token.cache.initialCapacity}")
	private int initialCapacity;

	@Value("${app.token.cache.maximumSize}")
	private long maximumSize;

	@Value("${app.token.cache.expireAfterAccessWithMinutes}")
	private long expireAfterAccessWithMinutes;

	private Cache<String, String> manualCache = Caffeine.newBuilder()
			.expireAfterAccess(expireAfterAccessWithMinutes, TimeUnit.MINUTES).initialCapacity(initialCapacity)
			.maximumSize(maximumSize).build();

	@Value("${app.token.ingoreTokenUrls}")
	private String[] ingoreTokenUrls;

	@Value("${app.token.userInfoKey}")
	private String userKey;

	@Value("${app.token.userInfoHeader}")
	private String userInfoHeader;

	@Autowired
	private Algorithm tokenAlgorithm;

	@Override
	public GatewayFilter apply(Object config) {

		return (exchange, chain) -> {
			try {
				final ServerHttpRequest request = exchange.getRequest();
				final ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
				builder.header("Access-Control-Allow-Origin", "*");// 允许跨域
				// builder.header("Access-Control-Allow-Methods",
				// ALLOWED_METHODS);
				// builder.header("Access-Control-Max-Age", MAX_AGE);
				// builder.header("Access-Control-Allow-Headers",
				// ALLOWED_HEADERS);
				// builder.header("Access-Control-Expose-Headers",
				// ALLOWED_Expose);
				// builder.header("Access-Control-Allow-Credentials", "true");

				final String url = request.getURI().getPath();
				for (String ingoreUrl : ingoreTokenUrls) {
					if (ingoreUrl.equals(url)) {// 对免检查token的url放行
						return chain.filter(exchange.mutate().request(builder.build()).build());
					}
				}

				final String accessToken = request.getHeaders().getFirst(headerName);
				if (!StringUtils.isEmpty(accessToken)) {

					// get不到的话对同一个key会阻塞，就是说即使多个线程同时请求该值也只会调用一次Function方法。这样可以避免与其他线程的写入竞争
					final String userInfoJson = manualCache.get(accessToken, key -> {

						try {
							final JWTVerifier verifier = JWT.require(tokenAlgorithm).build();
							final DecodedJWT decodedJWT = verifier.verify(accessToken);
							return decodedJWT.getClaim(userKey).asString();
						} catch (JWTVerificationException e) {
							e.printStackTrace();
							logger.error("token检验不通过：" + e.getMessage());
							return null;
						} catch (Exception e) {
							e.printStackTrace();
							logger.error(e.getMessage());
							return null;
						}

					});

					if (StringUtils.isEmpty(userInfoJson)) {
						return buildResponse(exchange, ResultCode.TOKEN_CHECK_ERR, ResultCode.TOKEN_CHECK_ERR_MSG);
					}

					builder.headers(httpHeaders -> {
						httpHeaders.remove(headerName);// 清除token
						try {
							httpHeaders.add(userInfoHeader, URLEncoder.encode(userInfoJson, "UTF-8"));// 将用户信息加入heder
						} catch (UnsupportedEncodingException e) {
							logger.error("URLEncoder.encode userInfoJson error:" + e.getMessage());
							throw new RuntimeException("URLEncoder.encode userInfoJson error:" + e.getMessage());
						}
					});

					return chain.filter(exchange.mutate().request(builder.build()).build());// 因为加入了header，需重新封装request后转发请求
				} else {
					return buildResponse(exchange, ResultCode.TOKEN_MISS, ResultCode.TOKEN_MISS_MSG);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
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
			logger.error(e.getMessage());
			bodyDataBuffer = response.bufferFactory()
					.wrap(JSONObject.toJSONString(new ResponseModel(code, msg)).getBytes());
			return response.writeWith(Mono.just(bodyDataBuffer));
		}

	}

}
