package com.zjc.onechat.inceptor;

import com.zjc.onechat.entity.UserHolder;
import com.zjc.onechat.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInceptor implements HandlerInterceptor {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public TokenInceptor(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider=jwtTokenProvider;
    }



    public TokenInceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("authorization");
        // 判断token是否为空
        if(token==null||token.equals("authorization")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        System.out.println("token:"+token);
        // 判断用户是否登录
        boolean logined = jwtTokenProvider.validateToken(token);

        if (!logined) {
             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        UserHolder.setUserId(jwtTokenProvider.getUserId(token));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.clearUserId();
    }
}
