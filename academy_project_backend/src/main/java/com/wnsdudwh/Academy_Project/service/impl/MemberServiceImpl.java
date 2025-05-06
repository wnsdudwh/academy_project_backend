package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.MemberDTO;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.service.MemberService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
public class MemberServiceImpl implements MemberService {
    private final MemberRepository mr;

    @Override
    public void saveMember(MemberDTO dto) {

        mr.save(Member.builder()
                .usernumber(UUID.randomUUID().toString())
                .userid(dto.getUserid())
                .userpw(dto.getUserpw())
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .build());
    }
}
