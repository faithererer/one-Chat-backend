package com.zjc.onechat.config;

import com.zjc.onechat.inceptor.TokenInceptor;
import com.zjc.onechat.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new  TokenInceptor(jwtTokenProvider))
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/user/avatar"
                );
    }

}
