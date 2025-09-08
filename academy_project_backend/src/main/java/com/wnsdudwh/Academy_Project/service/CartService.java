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

    /**
     * 장바구니 상품의 수량을 변경하는 메서드
     * @param memberId 현재 로그인된 회원의 ID (소유권 확인용)
     * @param cartItemId 수량을 변경할 장바구니 상품의 ID
     * @param quantity 새로운 수량
     */
    void updateItemQuantity(Long memberId, Long cartItemId, int quantity);

    /**
     * 장바구니 상품을 삭제하는 메서드
     * @param memberId 현재 로그인된 회원의 ID (소유권 확인용)
     * @param cartItemId 삭제할 장바구니 상품의 ID
     */
    void deleteCartItem(Long memberId, Long cartItemId);
}
