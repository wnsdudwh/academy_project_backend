package com.wnsdudwh.Academy_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOptionSaveDTO
{
    private String optionName;      // 예: Red
    private String optionType;      // 예: 컬러
    private Integer additionalPrice;    // 예: 20000
    private Integer stock;              // 재고

    @Builder.Default
    private boolean soldOut = false;        // 품절 여부, 기본값 false
}
