package com.wnsdudwh.Academy_Project.config.auth;

import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler
{
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        // 1. 인증 객체에서 PrincipalDetails를 가져옵니다.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        // 2. Member 객체로 JWT 토큰을 생성합니다.
        String token = jwtUtil.generateToken(member);

        // 3. 닉네임을 UTF-8 방식으로 URL을 인코딩.
        String encodeNickname = URLEncoder.encode(member.getNickname(), StandardCharsets.UTF_8);

        // 3. 토큰을 URL 파라미터에 담아 프론트엔드로 리디렉션 시킵니다.
        String targetUrl = "http://192.168.25.60:3000/oauth-redirect?token=" + token + "&nickname=" + encodeNickname + "&role=" + member.getRole().name();
        response.sendRedirect(targetUrl);
    }
}
