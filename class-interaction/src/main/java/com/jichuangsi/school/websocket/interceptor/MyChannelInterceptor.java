package com.jichuangsi.school.websocket.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



@Component
public class MyChannelInterceptor implements ChannelInterceptor {

	private static Map<String, String> idMap = new ConcurrentHashMap<>();
	public static int errCount = 0;

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
		// 判断客户端的连接状态
		switch (sha.getCommand()) {
		case CONNECT:
			// connect(sessionId,accountId);
			String idc = sha.getNativeHeader("accountId").get(0);
			if (StringUtils.isEmpty(idc)) {
				throw new RuntimeException("accountId must in header");
			}
			idMap.put(idc, idc);
			// System.out.println("=======================CONNECT:" +
			// sha.getNativeHeader("accountId").get(0));
		case CONNECTED:
			break;
		case DISCONNECT:
			// disconnect(sessionId,accountId,sha);
			break;
		case SUBSCRIBE:
			String ids = sha.getNativeHeader("accountId").get(0);
			if (StringUtils.isEmpty(ids)) {
				throw new RuntimeException("accountId must in header");
			}
			if (null == idMap.get(ids)) {
				inc();
				throw new RuntimeException("no id in server:" + ids);
			}
			// throw new RuntimeException("in preSend...");
			// System.out.println("=======================SUBSCRIBE:" +
			// sha.getDestination());
			// System.out.println("=======================SUBSCRIBE:" +
			// sha.getNativeHeader("accountId").get(0));

		default:
			break;
		}
		return message;
	}
	

	private static void inc() {
		synchronized (MyChannelInterceptor.class) {
			errCount++;
		}
	}

}
