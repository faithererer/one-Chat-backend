package com.zjc.onechat.config;

import com.zjc.onechat.im.CustomWebSocketHandler;
import com.zjc.onechat.inceptor.WebSocketInterceptor;
import com.zjc.onechat.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements  WebSocketConfigurer{

    private final CustomWebSocketHandler customWebSocketHandler;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    public WebSocketConfig(CustomWebSocketHandler customWebSocketHandler) {
        this.customWebSocketHandler = customWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler, "/chat")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .addInterceptors(new  WebSocketInterceptor(jwtTokenProvider)); // 注册拦截器
    }
}
