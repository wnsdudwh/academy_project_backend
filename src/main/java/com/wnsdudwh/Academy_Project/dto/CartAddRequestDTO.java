package com.wnsdudwh.Academy_Project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartAddRequestDTO
{
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    // 옵션은 없을 수도 있으므로 @NotNull은 제외
    private Long optionId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
}
