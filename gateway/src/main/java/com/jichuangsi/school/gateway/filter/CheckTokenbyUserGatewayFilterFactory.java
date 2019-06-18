package com.jichuangsi.school.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.gateway.model.ResponseModel;
import com.jichuangsi.school.gateway.service.CheckUrlService;
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
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
@Component
public class CheckTokenbyUserGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${app.token.headerName}")
    private String headerName;

    @Value("${app.token.cache.expireAfterAccessWithMinutes}")
    private long expireAfterAccessWithMinutes;

    @Value("${app.token.ingoreTokenUrls}")
    private String[] ingoreTokenUrls;

    @Value("${app.token.userInfoKey}")
    private String userClaim;

    @Autowired
    private Algorithm tokenAlgorithm;
    @Resource
    private CheckUrlService checkUrlService;
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            try {
                final ServerHttpRequest request = exchange.getRequest();
                final String url = request.getURI().getPath();
                if (checkUrlService.checkIsFreeUrl(url)){
                    return chain.filter(exchange);
                }
                final String accessToken = request.getHeaders().getFirst(headerName);
                if (!StringUtils.isEmpty(accessToken)) {
                    final JWTVerifier verifier = JWT.require(tokenAlgorithm).build();
                    verifier.verify(accessToken);// 校验有效性
                    // todo 校验有效期
                    //校验用户身份
                    DecodedJWT jwt= JWT.decode(accessToken);
                    String userId=jwt.getClaim(userClaim).asString();
                    UserInfoForToken userInfo = JSONObject.parseObject(userId, UserInfoForToken.class);
                    if (!checkUrlService.checkUserRole(userInfo, url)) {
                        return buildResponse(exchange, ResultCode.TOKEN_CHECK_ERR, ResultCode.NO_ACCESS);
                    }
                    return chain.filter(exchange);
                } else {
                    return buildResponse(exchange, ResultCode.TOKEN_MISS, ResultCode.TOKEN_MISS_MSG);
                }
            } catch (JWTVerificationException e) {
                logger.error("token检验不通过：" + e.getMessage());
                return buildResponse(exchange, ResultCode.TOKEN_CHECK_ERR, ResultCode.TOKEN_CHECK_ERR_MSG);
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
