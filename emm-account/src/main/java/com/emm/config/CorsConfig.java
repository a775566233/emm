package com.emm.config;

import com.emm.controller.jwt.JwtController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private static AppConfig appConfig;
    @Autowired
    public CorsConfig(AppConfig appConfig) {
        CorsConfig.appConfig = appConfig;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(appConfig.getWebCorsMapping()) // 所有接口
                .allowedOrigins(appConfig.getWebCorsAllowedOrigins()) // 支持域
                .allowCredentials(appConfig.isWebCorsAllowCredentials()) // 是否发送 Cookie
                .allowedOriginPatterns(appConfig.getWebCorsAllowedOriginPatterns()) // 支持域
                .allowedMethods(appConfig.getWebCorsAllowedMethods()) // 支持方法
                .allowedHeaders(appConfig.getWebCorsAllowedHeaders()) // 允许的原始请求头部信息
                .exposedHeaders(appConfig.getWebCorsExposedHeaders()); // 暴露的头部信息
    }
}
