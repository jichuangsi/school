package com.jichuangsi.school.classinteraction.websocket.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class MyChannelInterceptor implements ChannelInterceptor {

	private Log log = LogFactory.getLog(MyChannelInterceptor.class);
	
	@Value("${custom.token.userClaim}")
	private String userClaim;

	@Autowired
	private Algorithm tokenAlgorithm;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		// ignore non-STOMP messages like heartbeat messages
		if (sha.getCommand() == null) {
			return message;
		}

		// 这里的sessionId和accountId对应HttpSessionIdHandshakeInterceptor拦截器的存放key
		// String sessionId =
		// sha.getSessionAttributes().get(HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME).toString();
		// String accountId =
		// sha.getSessionAttributes().get("accountId").toString();
		
		String token;
		// 判断客户端的连接状态
		switch (sha.getCommand()) {
		case CONNECT:
			token = sha.getNativeHeader("token").get(0);
			checkToken(token);
			//todo 检查同一accessToken短时间内反复connect
			break;
		case CONNECTED:
			break;
		case DISCONNECT:
			break;
		case SUBSCRIBE:
			token = sha.getNativeHeader("token").get(0);
			checkToken(token);
			//todo 订阅时校验身份（例如学生不能订阅老师的相关主题）
			//DecodedJWT jwt = JWT.decode(token);
			//UserInfoForToken userInfo = JSONObject.parseObject(jwt.getClaim(userClaim).asString(),UserInfoForToken.class);
		default:
			break;
		}
		return message;
	}

	// 校验token
	private void checkToken(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			throw new RuntimeException("accountId must in header");
		}
		try {
			JWTVerifier verifier = JWT.require(tokenAlgorithm).build();
			verifier.verify(accessToken);
		} catch (JWTVerificationException e) {
			String msg = "when web socket connect,check accessToken error.";
			log.error(msg);
			e.printStackTrace();
			throw new RuntimeException(msg);
		}
	}

}
