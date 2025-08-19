package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Cart;
import com.wnsdudwh.Academy_Project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>
{
    // 회원은 하나의 장바구니만 가지므로, 반환 타입은 Optional<Cart> 이어야 함
    Optional<Cart> findByMember(Member member);
}
