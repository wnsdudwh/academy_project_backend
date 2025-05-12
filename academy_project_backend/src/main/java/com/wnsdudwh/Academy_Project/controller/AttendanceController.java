package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.entity.Attendance;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.AttendanceRepository;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController
{
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;
    private final JwtUtil jwtUtil;

    // ⭐ 출석 체크 API
    @PostMapping("/check")
    public ResponseEntity<?> checkAttendance(HttpServletRequest request)
    {
        // 1.JWT에서 유저 아이디 추출
        String token = jwtUtil.resolveToken(request);
        String userid = jwtUtil.extractUsername(token);

        // 2. 해당 유저 정보 가져오기
        Member member = memberRepository.findByUserid(userid);
        if (member == null)
        {
            return ResponseEntity.badRequest().body("입력된 사용자 정보를 찾을 수 없습니다.");
        }

        // 3. 오늘 날짜 확인
        LocalDate today = LocalDate.now();

        // 4. 오늘 날짜에 출석한 적 있는지 확인
        Optional<Attendance> result = attendanceRepository.findByMemberAndDate(member, today);
        if (result.isPresent())
        {
            return ResponseEntity.badRequest().body("오늘 이미 출석했습니다!");
        }

        // 5. 출석기록 없으면 출석 기록 저장
        Attendance attendance = Attendance.builder()
                .member(member)
                .date(LocalDate.now())
                .build();
        attendanceRepository.save(attendance);

        //  6. 포인트 +50 지급
        int currentPoint = member.getPoint();   //  현재포인트 갖고옴
        member.setPoint(currentPoint + 50);     // 갖고온 포인트에 +50원
        memberRepository.save(member);         // 작업 후 저장

        return ResponseEntity.ok("출석 완료! [50 포인트]가 지급되었습니다.");
    }
}
