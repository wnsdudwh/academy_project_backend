package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Attendance;
import com.wnsdudwh.Academy_Project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>
{
    Optional<Attendance> findByMemberAndDate(Member member, LocalDate date);    // 오늘 출석했는지??

    List<Attendance> findByMemberAndDateBetween(Member member, LocalDate startDate, LocalDate endDate); //  달력에 출석 한 날짜 표시
}
