package com.wnsdudwh.Academy_Project.config;

import com.wnsdudwh.Academy_Project.config.auth.CustomOAuth2UserService;
import com.wnsdudwh.Academy_Project.config.auth.OAuth2LoginSuccessHandler;
import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetailsService;
import com.wnsdudwh.Academy_Project.config.jwt.JwtAuthenticationEntryPoint;
import com.wnsdudwh.Academy_Project.config.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;  // Oauth2.0 구글 로그인
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;  // Oauth2.0 로그인 성동 처리 전담 주입

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

                // 예외 처리 설정
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // oauth2Login 설정 추가
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                        // 소셜 로그인 성공 시 후처리할 서비스 지정
                                        .userService(customOAuth2UserService)
                        )
//                        .loginPage("/login")    // 인증이 필요한 페이지 접근 시 리디렉션 경로 (프론트엔드 로그인 페이지, 현재는 사용 안함)
//                        .defaultSuccessUrl("http://192.168.25.60:3000") // 로그인 성공 후 이동할 프론트엔드 주소 (현재는 사용 안함)
                        // 로그인 성공 시 JWT 토큰을 생성하고, 프론트로 리디렉션 시킬 핸들러 지정
                        .successHandler(oAuth2LoginSuccessHandler)
                        // 로그인 실패 시 이동할 프론트엔드 주소
                        .failureUrl("http://192.168.25.60:3000/login")
                )

                // ✅ 3. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1. [가장 구체적인 규칙] 관리자만 접근 가능한 상품 관리 API
                        .requestMatchers(
                                "/api/products/register",
                                "/api/products/update/**",
                                "/api/products/{id}/visibility",
                                "/api/products/{id}/status",
                                "/api/products/{id}/soft-delete"
                        ).hasRole("ADMIN")

                        // 2. [그 다음 구체적인 규칙] 상품 조회(GET)는 누구나 가능
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // 3. [기타 공개 경로] 기존에 있던 다른 공개 경로들
                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/api/brand/**",
                                "/api/category/**",
                                "/upload/**",
                                "/favicon.ico"
                        ).permitAll()

                        // 4. [기타 관리자 경로] /admin/으로 시작하는 다른 경로가 있을 경우를 대비
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 5. [가장 마지막] 위에서 지정하지 않은 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // ✅ 4. JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
