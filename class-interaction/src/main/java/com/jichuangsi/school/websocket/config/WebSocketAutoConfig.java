package com.jichuangsi.school.websocket.config;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.tcp.TcpOperations;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import reactor.ipc.netty.options.ClientOptions;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAutoConfig implements WebSocketMessageBrokerConfigurer {

	//@Value("${msg.stompBrokerRelay.hostIp}")
	//private String hostIP;
	//@Value("${msg.stompBrokerRelay.port}")
	//private int port;
	
	//@Resource
	//private HandshakeInterceptor handshakeInterceptor;
	//@Resource
	//private ChannelInterceptor channelInterceptor;

//	private static SocketAddress address1;
//	private static SocketAddress address2;
//	private static Map<String, SocketAddress> addressMap;

//	public WebSocketAutoConfig() {
//		try {
//			address1 = new InetSocketAddress(InetAddress.getByName("192.168.10.71"), 61613);
//			address2 = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 61613);
//			addressMap = new HashMap<>();
//			addressMap.put("127.0.0.1", address2);
//			addressMap.put("192.168.10.71", address1);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			throw new RuntimeException("fail to connetc MessageBroker:" + e.getMessage());
//		}
//	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/course") // 开启/course端点
				.setAllowedOrigins("*") // 允许跨域访问
				//.addInterceptors(handshakeInterceptor)//如需拦截seesion，例如判断是否有登录，可自定义
				//.addInterceptors(new HttpSessionHandshakeInterceptor())
				.withSockJS(); // 使用sockJS
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue/","/topic/");
		// //Broker名称，queue对单，topic订阅
		// registry.setApplicationDestinationPrefixes("/app"); //客户端发消息时需要加的前缀
		// registry.enableStompBrokerRelay("/topic/").setTcpClient(createTcpClient());
		//registry.enableStompBrokerRelay("/queue/","/topic/").setRelayHost(hostIP).setRelayPort(port);
		

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// 发送前拦截可在此处处理，或独立定义 ChannelInterceptor实现类注入，如创建连接时的权限控制
		//registration.interceptors(channelInterceptor);	
	}

	// private TcpOperations<byte[]> createTcpClient() {
	//
	// Consumer<ClientOptions.Builder<?>> builderConsumer = builder -> {
	// builder.connectAddress(() -> {
	// Map<String, Long> errorMap =
	// BrokerAvailabilityListener.getBrokerErrorMap();
	// boolean flag = true;
	// while(flag){
	// String key = null;
	// if (0 == System.currentTimeMillis() % 2) {
	// key = "127.0.0.1";
	// }else{
	// key = "192.168.10.71";
	// }
	// Long errValTime = errorMap.get(key);
	// if(null==errValTime || System.currentTimeMillis()>errValTime){
	// System.out.println("=================in createTcpClient:"+key);
	// return addressMap.get(key);
	// }
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	//
	//
	// //自定义外部MessageBroker负载均衡，需要更详细的failover和故障恢复设置
	//// if (0 == System.currentTimeMillis() % 2) {
	//// System.out.println("=================in
	// createTcpClient:"+address1.toString());
	//// return address1;
	//// } else {
	//// System.out.println("=================in
	// createTcpClient:"+address2.toString());
	//// return address2;
	//// }
	//
	// });
	// };
	//
	// return new ReactorNettyTcpClient<>(builderConsumer, new
	// StompReactorNettyCodec());
	// }

}
