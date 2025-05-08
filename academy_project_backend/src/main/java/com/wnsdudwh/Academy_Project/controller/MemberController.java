package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.MemberDTO;
import com.wnsdudwh.Academy_Project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")   // 이 컨트롤러의 기본 경로: /api/...
@RequiredArgsConstructor
public class MemberController
{
    private final MemberService ms;

    @PostMapping("/register")
    public ResponseEntity<?> saveMember(@RequestBody MemberDTO dto)
    {
        ms.saveMember(dto);
        return ResponseEntity.ok("유저 저장됨");
    }

}
