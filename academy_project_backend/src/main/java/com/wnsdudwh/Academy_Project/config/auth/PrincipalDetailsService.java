package com.wnsdudwh.Academy_Project.config.auth;

import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService
{
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException
    {
        Member member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + userid));

        return new PrincipalDetails(member); // 이제 Member 엔티티를 담은 PrincipalDetails를 반환
    }
}
