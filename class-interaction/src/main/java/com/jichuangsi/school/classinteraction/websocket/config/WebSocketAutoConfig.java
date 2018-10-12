package com.jichuangsi.school.classinteraction.websocket.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAutoConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${custom.ws-endpoint}")
	private String endpointPath;

	@Resource
	private ChannelInterceptor channelInterceptor;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(endpointPath) // 开启/course端点
				.setAllowedOrigins("*") // 允许跨域访问
				// .addInterceptors(new
				// HttpSessionHandshakeInterceptor())//如需拦截seesion，例如判断是否有登录，可自定义实现HandshakeInterceptor接口或继承HttpSessionHandshakeInterceptor
				.withSockJS(); // 使用sockJS
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue/", "/topic/");// Broker名称，queue对单，topic订阅
		// registry.setApplicationDestinationPrefixes("/app"); //客户端发消息时需要加的前缀
		// registry.enableStompBrokerRelay("/topic/").setTcpClient(createTcpClient());
		// registry.enableStompBrokerRelay("/queue/","/topic/").setRelayHost(hostIP).setRelayPort(port);

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// 发送前拦截可在此处处理，或独立定义 ChannelInterceptor实现类注入，如创建连接时的权限控制
		registration.interceptors(channelInterceptor);
	}
}
