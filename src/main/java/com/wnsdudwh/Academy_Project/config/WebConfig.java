package com.wnsdudwh.Academy_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOrigins
                (
                        "http://localhost:3000",
                        "http://3.34.46.2:3000",     // AWS 서버 Public IP
                        "http://192.168.25.60:3000",   // 내 PC Private IP
                        "http://180.64.38.234:3000"    // 내 PC Public IP
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // 예: http://localhost:8080/upload/products/GT-005/thumb.jpg
        registry.addResourceHandler("/upload/**")   //요청경로   /upload/...
                .addResourceLocations("file:///D:/upload/");    //실제 폴더의 경로 (file:// + 절대경로 /D:/...)
    }

}