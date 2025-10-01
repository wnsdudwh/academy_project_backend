package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.MemberDTO;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.service.MemberService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
public class MemberServiceImpl implements MemberService
{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveMember(MemberDTO dto)
    {
        // 🔍 닉네임 중복 체크
        if (memberRepository.findByNickname(dto.getNickname()).isPresent())
        {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        Member member = Member.builder()
                .usernumber(UUID.randomUUID().toString())
                .userid(dto.getUserid())
                .userpw(passwordEncoder.encode(dto.getUserpw()))
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .build();

        memberRepository.save(member);
//        memberRepository.save(Member.builder()
//                .usernumber(UUID.randomUUID().toString())
//                .userid(dto.getUserid())
//                .userpw(dto.getUserpw())
//                .username(dto.getUsername())
//                .nickname(dto.getNickname())
//                .build());
    }
}
