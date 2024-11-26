package com.zjc.onechat.inceptor;

import com.zjc.onechat.entity.UserHolder;
import com.zjc.onechat.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public WebSocketInterceptor(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider=jwtTokenProvider;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = request.getHeaders().getFirst("authorization");
        System.out.println(request.getHeaders().toString());
        System.out.println("ws"+token);
      // 判断token是否为空
        if(token==null||token.equals("authorization")){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        System.out.println("wstoken:"+token);
        System.out.println(jwtTokenProvider.createToken(1L));
        System.out.println(jwtTokenProvider.createToken(2L));

        // 判断用户是否登录
        boolean logined = jwtTokenProvider.validateToken(token);
        if (!logined) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        Long userId = jwtTokenProvider.getUserId(token);
        attributes.put("userId", userId);
        UserHolder.setUserId(userId);
        System.out.println("USERHOLDER存储:"+UserHolder.getUserId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // 握手成功后的处理逻辑，可以在这里进行一些清理工作
        System.out.println("握手成功");
    }
}
