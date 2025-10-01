package com.wnsdudwh.Academy_Project.util;

import com.wnsdudwh.Academy_Project.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil
{
    // 🔐 시크릿 키 (길고 랜덤한 문자열로 설정) -> 프로퍼티 파일에서 값을 주입받게 수정
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // ⏰ 토큰 만료시간 (예: 1시간 [현 24시간])
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    public String generateToken(Member member)
    {
        Claims claims = Jwts.claims();
        claims.setSubject(member.getUserid());
        claims.put("role", member.getRole().name());
        claims.put("nickname", member.getNickname());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // 🔑 JWT 발급 메서드

    // ✅ JWT 검증
    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e)
        {
            return false;
        }
    }

    // ✅ JWT에서 사용자 정보 추출
    public String getUsernameFromToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 🔐 시크릿 키 생성
    private Key getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // request.getHeader()에서 JWT 토큰을 뽑아오는 메서드
    public String resolveToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7); // "Bearer " 이후 문자열 반환
        }

        return null;
    }
}
