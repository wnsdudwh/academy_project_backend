package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.MemberDTO;
import com.wnsdudwh.Academy_Project.entity.Attendance;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.AttendanceRepository;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.service.MemberService;
import com.wnsdudwh.Academy_Project.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")   // 이 컨트롤러의 기본 경로: /api/...
@RequiredArgsConstructor
public class MemberController
{
    private final MemberService ms;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final AttendanceRepository attendanceRepository;

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


    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPageInfo(HttpServletRequest request)
    {
        // 1. 토큰에서 아이디 추출
        String token = jwtUtil.resolveToken(request);
        String userId = jwtUtil.extractUsername(token);

        // 2. DB에서 사용자 정보 가져오기
        Member member = memberRepository.findByUserid(userId);
        if (member == null)
        {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        // 3. 응답에 필요한 정보만 담기
        Map<String, Object> response = new HashMap<>();
        response.put("userId", member.getUserid());
        response.put("nickname", member.getNickname());
        response.put("regDate", member.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.put("point", member.getPoint());

        return ResponseEntity.ok(response);
    }

    // 📌 닉네임/비밀번호 수정 API
    @PutMapping("/mypage/update")
    public ResponseEntity<?> updateMember(@RequestBody MemberDTO dto, HttpServletRequest request)
    {
        // 1. 토큰에서 아이디 추출
        String token = jwtUtil.resolveToken(request);
        String userId = jwtUtil.extractUsername(token);

        // 2. DB에서 사용자 정보 가져오기
        Member member = memberRepository.findByUserid(userId);
        if (member == null)
        {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        // ✅ 3. 닉네임 변경
        if (dto.getNickname() != null && !dto.getNickname().isEmpty())
        {
            member.setNickname(dto.getNickname());
        }

        // ✅ 4. 비밀번호 변경 (암호화 필수!)
        if (dto.getUserpw() != null && !dto.getUserpw().isEmpty())
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            member.setUserpw(passwordEncoder.encode(dto.getUserpw()));
        }

        // ✅ 5. 저장
        memberRepository.save(member);

        return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다!");
    }

}
