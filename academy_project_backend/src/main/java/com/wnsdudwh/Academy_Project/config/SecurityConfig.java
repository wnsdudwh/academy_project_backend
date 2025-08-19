package com.wnsdudwh.Academy_Project.config;

import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetailsService;
import com.wnsdudwh.Academy_Project.config.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final PrincipalDetailsService principalDetailsService;  // 주입받는 대상을 변경
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .formLogin(form -> form.disable()) // Form 로그인 방식 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 방식 비활성화

                // ✅ 2. 세션을 사용하지 않도록 설정 (JWT 사용 시 필수)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ 3. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 아래 경로들은 인증 없이 접근 허용
                        .requestMatchers("/auth/**", "/attendance/**", "/products/**").permitAll()
                        // 그 외 모든 경로는 인증 필요
                        .anyRequest().authenticated()
                )
                // ✅ 4. JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
