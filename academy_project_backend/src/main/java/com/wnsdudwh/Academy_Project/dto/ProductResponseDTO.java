package com.wnsdudwh.Academy_Project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String categoryName;

    private List<String> subImages;

    @Builder.Default
    private List<ProductOptionResponseDTO> options = new ArrayList<>();

    // 상품 표시 여부
    private boolean visible;
    // 상품의 신상품 여부
    private boolean newProduct;
    // 상품이 게시될 날짜
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    // 상품의 한줄 태그 ex)#펜더, ~~
    private String tags;
}
