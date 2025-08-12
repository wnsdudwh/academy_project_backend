package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Cart;
import com.wnsdudwh.Academy_Project.entity.CartItem;
import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemrepository extends JpaRepository<CartItem, Long>
{
    // 장바구니, 상품, 옵션이 모두 일치하는 항목을 찾는 메서드
    // -> 이미 장바구니에 담긴 상품인지 확인할 때 사용
    Optional<CartItem> findByCartAndProductAndProductOption(Cart cart, Product product, ProductOption option);

    // 상품 옵션이 없는 경우를 위한 메서드 오버로딩
    Optional<CartItem> findByCartAndProductAndProductOptionIsNull(Cart cart, Product product);
}
