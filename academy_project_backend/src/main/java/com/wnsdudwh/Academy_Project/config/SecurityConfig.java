package com.wnsdudwh.Academy_Project.config;

import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetailsService;
import com.wnsdudwh.Academy_Project.config.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                .cors(Customizer.withDefaults())    //  CORS 설정 활성화
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .formLogin(form -> form.disable()) // Form 로그인 방식 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 방식 비활성화

                // ✅ 2. 세션을 사용하지 않도록 설정 (JWT 사용 시 필수)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ 3. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1. 누구나 접근 가능한 "공개" 경로들
                        .requestMatchers(
                                "/auth/**",          // 로그인, 회원가입
                                "/api/products/**",      // 상품 관련 페이지/API (일반 사용자용)
                                "/api/brand/**",     // 브랜드 API
                                "/api/category/**",  // 카테고리 API
                                "/upload/**"         // 이미지 파일
                        ).permitAll()

                        // 2. "ADMIN" 역할이 필요한 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 3. 위에서 설정한 경로를 제외한 "나머지 모든 경로"는 인증(로그인)이 필요함
                        .anyRequest().authenticated()
                )
                // ✅ 4. JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
