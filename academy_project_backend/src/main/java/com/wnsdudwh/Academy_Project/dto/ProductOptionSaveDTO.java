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
    private int additionalPrice;    // 예: 20000
    private int stock;              // 재고
    private boolean soldOut;        // 품절 여부
}
