package com.wnsdudwh.Academy_Project.config.jwt;

import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetailsService;
import com.wnsdudwh.Academy_Project.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter
{
    private final JwtUtil jwtUtil;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        System.out.println("====== [JwtRequestFilter] START ======"); // ✅ 발자국 1

        // 1. 요청 헤더에서 토큰 추출
        String token = jwtUtil.resolveToken(request);

        // 2. 토큰 유효성 검사
        if (token != null && jwtUtil.validateToken(token))
        {
            // 토큰이 유효하면 사용자 정보 로드
            String userid = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = principalDetailsService.loadUserByUsername(userid);

            // SecurityContext에 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        System.out.println("====== [JwtRequestFilter] END ======"); // ✅ 발자국 2
        // 3. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
