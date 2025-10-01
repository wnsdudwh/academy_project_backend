package com.wnsdudwh.Academy_Project.dto;

import com.wnsdudwh.Academy_Project.entity.CartItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponseDTO
{
    private Long cartItemId;
    private Long productId;
    private String productName;
    private Long optionId;
    private String optionName; // 옵션 이름 (예: "색상: 레드")
    private int quantity;
    private boolean selected;
    private String thumbnailUrl;
    private int price; // 옵션 가격이 포함된 최종 단가
    private int totalPrice; // 수량 * 최종 단가

    // Entity를 DTO로 변환하는 정적 메서드 (서비스에서 사용)
    public static CartItemResponseDTO fromEntity(CartItem cartItem)
    {
        // 옵션이 있을 때와 없을 때를 구분
        int finalPrice = cartItem.getProduct().getPrice();
        String finalOptionName = "옵션 없음";

        if (cartItem.getProductOption() != null)
        {
            finalPrice += cartItem.getProductOption().getAdditionalPrice();
            finalOptionName = cartItem.getProductOption().getOptionName();
        }

        return CartItemResponseDTO.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .optionId(cartItem.getProductOption() != null ? cartItem.getProductOption().getId() : null)
                .optionName(finalOptionName)
                .quantity(cartItem.getQuantity())
                .selected(cartItem.isSelected())
                .thumbnailUrl(cartItem.getProduct().getThumbnailUrl()) // 예시 필드
                .price(finalPrice)
                .totalPrice(finalPrice * cartItem.getQuantity())
                .build();
    }
}