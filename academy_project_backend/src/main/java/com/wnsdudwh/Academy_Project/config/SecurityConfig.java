package com.wnsdudwh.Academy_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/register", "/test").permitAll()
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
