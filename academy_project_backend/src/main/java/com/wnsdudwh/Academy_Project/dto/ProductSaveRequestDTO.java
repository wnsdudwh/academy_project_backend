package com.wnsdudwh.Academy_Project.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
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
    // 상품 옵션리스트 추가  프론트에서 JSON.stringify()로 넘기는 옵션 문자열
    private String options;
    // 실제 사용할 파싱된 옵션 리스트 (transient로 직렬화 제외)
    private transient List<ProductOptionSaveDTO> optionList;
    //  파일 필드 추가
    private MultipartFile thumbnail;
    private List<MultipartFile> subImages;
    // 상품 표시 여부
    private boolean visible;
    // 상품의 신상품 여부
    private boolean newProduct;
    // 상품이 게시될 날짜
    private LocalDate releaseDate;
    // 상품의 한줄 태그 ex)#펜더, ~~
    private String tags;

    public void parseOptions()
    {
        if (this.options != null && !this.options.isBlank()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.optionList = mapper.readValue(this.options,
                        new TypeReference<List<ProductOptionSaveDTO>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException("옵션 JSON 파싱 실패", e);
            }
        }
    }
}
