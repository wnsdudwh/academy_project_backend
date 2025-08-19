package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetails;
import com.wnsdudwh.Academy_Project.dto.CartAddRequestDTO;
import com.wnsdudwh.Academy_Project.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController
{
    private final CartService cartService;  // 구현체가 아닌 인터페이스를 주입받음

    // TODO: 1. 장바구니 상품 추가 API
    @PostMapping("/items")
    public ResponseEntity<String> addItemToCart(@Valid @RequestBody CartAddRequestDTO requestDTO, @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        System.out.println("====== [CartController] addItemToCart START ======"); // ✅ 발자국 3
        // Spring Security 컨텍스트에서 인증된 사용자 정보 가져오기
        Long memberId = principalDetails.getMember().getIdx();

        cartService.addItemToCart(memberId, requestDTO);

        return ResponseEntity.ok("장바구니에 상품이 추가되었습니다.");
    }

    // TODO: 2. 장바구니 상품 조회 API
    // TODO: 3. 장바구니 상품 수량 변경 API
    // TODO: 4. 장바구니 상품 삭제 API

}
