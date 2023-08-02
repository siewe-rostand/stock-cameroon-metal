package com.siewe.inventorymanagementsystem.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogHandlerInterceptor()).order(2);
        registry.addInterceptor(new BasicAuthHandlerInterceptor()).order(1);
    }
}
