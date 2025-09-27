package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long>
{
    // 특정 회원의 모든 주소록을 찾는 메서드
    List<Address> findByMemberIdx(Long memberIdx);
}
