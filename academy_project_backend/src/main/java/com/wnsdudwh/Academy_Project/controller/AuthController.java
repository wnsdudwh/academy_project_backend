package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.LoginRequestDTO;
import com.wnsdudwh.Academy_Project.dto.LoginResponseDTO;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController   // REST API 전용 컨트롤러. @Controller + @ResponseBody 포함됨
@RequestMapping("/auth")   // 이 컨트롤러의 기본 경로: /auth/...
@RequiredArgsConstructor  // final로 선언된 멤버변수를 생성자로 주입해줌
public class AuthController
{
    // 🔐 DB에서 사용자 찾을 때 사용할 JPA Repository
    private final MemberRepository memberRepository;

    // 🔑 JWT 발급/검증용 클래스
    private final JwtUtil jwtUtil;

    // 🧂 비밀번호 암호화 객체 (BCrypt 방식)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 📬 POST /api/login 요청 처리 (로그인 요청)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        // ✅ 1. 입력받은 아이디로 회원 정보 조회
        Member member = memberRepository.findByUserid(loginRequestDTO.getUserid());

        // ❌ 회원이 없으면 에러 응답
        if (member == null)
        {
            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
        }

        // ✅ 2. 입력한 비밀번호와 DB의 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(loginRequestDTO.getUserpw(), member.getUserpw()))
        {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        // ✅ 2.5. 로그인 성공 → 마지막 로그인 시간 저장
        // ⭐ 초까지만 자르고 다시 LocalDateTime 객체로 만들어 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        LocalDateTime truncatedTime = LocalDateTime.parse(formattedTime, formatter);

        member.setLastLogin(truncatedTime);
        memberRepository.save(member);

        // ✅ 3. 로그인 성공 → JWT 토큰 발급 -> ~
        String token = jwtUtil.generateToken(member.getUserid());

        // ✅ 4. 토큰과 닉네임을 DTO에 담아서 응답
        LoginResponseDTO response = new LoginResponseDTO(token, member.getNickname());

        return ResponseEntity.ok(response); // 200 OK + 토큰+닉네임 반환
    }
}

