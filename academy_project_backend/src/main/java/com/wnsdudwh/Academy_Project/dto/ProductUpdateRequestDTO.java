package com.wnsdudwh.Academy_Project.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductUpdateRequestDTO
{
    // productCode는 보통 수정하지 않음 (PK나 유니크키일 경우)
    // id값은 url에서 받아옴
    private String name;
    private int price;
    private boolean discount;
    private BigDecimal discountRate;
    private BigDecimal pointRate;
    private int shippingFee;
    private int stockTotal;
    private String status;
    private String shortDescription;

    private Long brandId;
    private Long categoryId;

    // 📌 수정 시 첨부할 새 이미지들
    private MultipartFile thumbnail;
    private List<MultipartFile> subImages;
}
