package com.wnsdudwh.Academy_Project.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO
{
    private Long cartId;
    private List<CartItemResponseDTO> items; // 장바구니에 담긴 상품 목록
    private int totalAmount; // 장바구니 전체 주문 예상 금액
}