package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.MemberDTO;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")   // 이 컨트롤러의 기본 경로: /api/...
@RequiredArgsConstructor
public class MemberController
{
    private final MemberService ms;
    private final MemberRepository memberRepository;

    @PostMapping("/register")
    public ResponseEntity<?> saveMember(@RequestBody MemberDTO dto)
    {
        try
        {
            ms.saveMember(dto);
            return ResponseEntity.ok("회원가입 완료!");
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname)
    {
        boolean exists = memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(!exists); // true = 사용 가능, false = 이미 있음
    }


}
