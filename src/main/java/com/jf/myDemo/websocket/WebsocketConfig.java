package com.jf.myDemo.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-04 10:44
 * @Description: WebSocket配置处理器
 * To change this template use File | Settings | File and Templates.
 */

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws").addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(myHandler(), "/ws/sockjs").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
    }

    @Bean
    public WebsocketHandler myHandler() {
        return new WebsocketHandler();
    }
}
