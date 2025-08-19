package com.wnsdudwh.Academy_Project.service;

import com.wnsdudwh.Academy_Project.dto.CartAddRequestDTO;
import com.wnsdudwh.Academy_Project.dto.CartResponseDTO;

public interface CartService
{
    /**
     * 장바구니에 상품을 추가하는 메서드
     * @param memberId 현재 로그인된 회원의 ID
     * @param requestDTO 추가할 상품의 정보 (productId, optionId, quantity)
     */
    void addItemToCart(Long memberId, CartAddRequestDTO requestDTO);

    /**
     * 회원의 장바구니 정보를 조회하는 메서드
     * @param memberId 현재 로그인된 회원의 ID
     * @return 장바구니 정보가 담긴 DTO
     */
    CartResponseDTO getCartByMemberId(Long memberId);

    // TODO: 3. 장바구니 상품 수량 변경
    // TODO: 4. 장바구니 상품 삭제
}
