package com.wnsdudwh.Academy_Project.config.auth;

import com.wnsdudwh.Academy_Project.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User
{
    private final Member member;
    private Map<String, Object> attributes; // Oauth2.0 추가를 위한 Map 필드 추가

    // 1. 일반 로그인을 위한 생성자 (기존 코드)
    public PrincipalDetails(Member member)
    {
        this.member = member;
    }

    // 2. OAuth2 로그인을 위한 새로운 생성자
    public PrincipalDetails(Member member, Map<String, Object> attributes)
    {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> member.getRole().name());
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return member.getUserpw();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // --- OAuth2User 오버라이드 메서드들 ---
    @Override
    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    @Override
    public String getName()
    {
        // 보통 사용자의 고유 식별자를 반환합니다. 여기서는 이메일(userid)을 사용.
        return member.getUserid();
    }
    // --- OAuth2User 오버라이드 메서드들 ---

    // 계정이 만료되지 않았는지 리턴 (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지 리턴 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지 리턴 (true: 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용가능)인지 리턴 (true: 활성화)
    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
