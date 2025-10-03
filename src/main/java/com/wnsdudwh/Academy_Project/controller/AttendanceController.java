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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
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
        if (token == null)
        {
            return ResponseEntity.badRequest().body("토큰이 없습니다.");
        }
        String userid = jwtUtil.getUsernameFromToken(token);

        // 2. 해당 유저 정보를 Optional<Member> (상자)에 담아 가져오기
        Optional<Member> optionalMember = memberRepository.findByUserid(userid);

        // 3. 상자가 비어있는지 (회원이 없는지) 확인
        if (optionalMember.isEmpty()) // (수정) .isEmpty()로 확인
        {
            return ResponseEntity.badRequest().body("입력된 사용자 정보를 찾을 수 없습니다.");
        }

        // 4. 상자에서 실제 Member 객체(내용물) 꺼내기
        Member member = optionalMember.get(); // (수정) .get()으로 꺼내기
        LocalDate today = LocalDate.now();


        // 5. 꺼낸 Member 객체를 사용 해 오늘 날짜에 출석한 적 있는지 확인
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

    // 출석판에 출석 한 날짜 V 표시
    @GetMapping("/list")
    public ResponseEntity<?> getAttendanceList(HttpServletRequest request, @RequestParam int year, @RequestParam int month)
    {
        String token = jwtUtil.resolveToken(request);
        if (token == null)
        {
            return ResponseEntity.badRequest().body("토큰이 없습니다.");
        }
        String userid = jwtUtil.getUsernameFromToken(token);

        Optional<Member> optionalMember = memberRepository.findByUserid(userid);
        if (optionalMember.isEmpty())
        {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();

        // 해당 월의 시작일과 마지막일 구하기
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 출석 기록 조회
        List<Attendance> attendances = attendanceRepository.findByMemberAndDateBetween(member, startDate, endDate);

        // 날짜만 추출해서 문자열 리스트로 반환
        List<String> attendedDates = attendances.stream()
                .map(att -> att.getDate().toString())
                .toList();

        return ResponseEntity.ok().body(Map.of("dates", attendedDates));
    }
}
