package com.wnsdudwh.Academy_Project.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO
{
    // 프론트에 내려줄 정보만 담는 DTO
    private Long id;
    private String productCode;
    private String name;
    private int price;
    private int discountPrice;
    private boolean discount;
    private BigDecimal discountRate;
    private BigDecimal pointRate;
    private int shippingFee;
    private int stockTotal;
    private String status;
    private String thumbnailUrl;
    private String shortDescription;

    private String brandName;
    private String categoryName;

    private List<String> subImages;
}
