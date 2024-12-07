package com.emm.controller.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtFilter implements WebMvcConfigurer {
    private JwtController jwtController;

    @Autowired
    public void setJwtController(JwtController jwtController) {
        this.jwtController = jwtController;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtController)
                .addPathPatterns("/**");//拦截所有路径
    }
}
