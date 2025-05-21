package com.wnsdudwh.Academy_Project.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSaveRequestDTO
{
    // 상품 코드 (예: GT-001)
    private String productCode;

    // 상품명
    private String name;

    // 가격
    private int price;

    // 할인 여부
    private boolean discount;

    // 할인율
    private BigDecimal discountRate;

    // 적립률
    private BigDecimal pointRate;

    // 배송비
    private int shippingFee;

    // 총 재고 수량
    private int stockTotal;

    // 상태 (예: "AVAILABLE", "SOLD_OUT")
    private String status;

    // 썸네일 이미지 URL
    private String thumbnailUrl;

    // 간략 설명
    private String shortDescription;

    // 브랜드 ID (🔗 Brand 연관관계)
    private Long brandId;

    // 카테고리 ID (🔗 Category 연관관계)
    private Long categoryId;
}
