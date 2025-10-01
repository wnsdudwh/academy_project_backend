package com.wnsdudwh.Academy_Project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionResponseDTO
{
    private String optionName;      // 예: Red
    private String optionType;      // 예: 컬러
    private Integer additionalPrice;    // 예: 20000
    private Integer stock;              // 재고
    private boolean soldOut;        // 품절 여부, 기본값 false
}
