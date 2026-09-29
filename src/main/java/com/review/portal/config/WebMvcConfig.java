package com.review.portal.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC Configuration.
 * Configures static resources, view resolvers, and route interceptors
 * for both Employee and Manager portals independently.
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final ManagerAuthInterceptor managerAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Employee Route Protection
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/dashboard", "/review", "/review/**", "/employee/**")
                .excludePathPatterns(
                        "/login",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/api/**"
                );

        // Manager Route Protection (Phase 6.1)
        registry.addInterceptor(managerAuthInterceptor)
                .addPathPatterns("/manager/**")
                .excludePathPatterns(
                        "/manager/login",
                        "/manager/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
