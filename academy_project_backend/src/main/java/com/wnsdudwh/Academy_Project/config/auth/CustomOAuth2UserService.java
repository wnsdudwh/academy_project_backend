package com.wnsdudwh.Academy_Project.config.auth;

import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.entity.Role;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService
{
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        // 1. 구글로부터 받은 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. 사용자 정보를 바탕으로 우리 DB에 사용자가 있는지 확인합니다.
        String userid = (String) attributes.get("email");   // 구글 이메일을 우리 시스템의 userid로 사용
        String username = (String) attributes.get("name");

        Optional<Member> optionalMember = memberRepository.findByUserid(userid);
        Member member;

        if (optionalMember.isPresent())
        {
            // 3. 이미 가입된 회원이라면, 정보를 업데이트합니다.
            member = optionalMember.get();
            member.setUsername(username);
            memberRepository.save(member);
        }
        else
        {
            // 4. 처음 방문한 사용자라면, 자동으로 회원가입을 시킵니다.
            member = Member.builder()
                    .userid(userid)
                    .username(username)
                    .userpw(UUID.randomUUID().toString())   // 비밀번호는 임의의 값으로 설정
                    .nickname(username)
                    .role(Role.ROLE_USER)   // 기본 ROLE는 USER
                    .build();
            memberRepository.save(member);
        }

        // 5. 최종적으로 우리 시스템의 인증 객체인 PrincipalDetails를 반환합니다.
        return new PrincipalDetails(member, attributes);
    }
}
