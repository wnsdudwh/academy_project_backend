package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>
{
    Member findByUserid(String userid);
    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
