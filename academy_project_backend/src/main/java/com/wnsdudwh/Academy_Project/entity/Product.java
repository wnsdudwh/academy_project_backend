package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    // 상품 고유 식별자 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외부 노출용 상품 코드 (예: GT-001)
    @Column(nullable = false, unique = true, length = 30)
    private String productCode;

    // 상품명
    @Column(nullable = false, length = 100)
    private String name;

    // 가격
    @Column(nullable = false)
    private int price;

    // 할인 여부
    @Column(nullable = false)
    private boolean discount;

    // 할인율? (예 : 15.5%)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discountRate;

    // 적립률 (예 : 2.5%)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal pointRate;

    // 기본 배송료?
    @Column(nullable = false)
    private int shippingFee;

    // 총 재고 수량
    @Column(nullable = false)
    private int stockTotal;

    // 상태 (판매중, 품절 등)
    @Column(nullable = false, length = 20)
    private String status;

    // 썸네일 이미지 URL
    @Column(length = 255)
    private String thumbnailUrl;

    // 간단한 설명
    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    // 조회수
    @Column(nullable = false)
    private int viewCount;

    // 누적 판매량
    @Column(nullable = false)
    private int soldCount;

    // 등록일
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 🔗 브랜드 연관관계 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    // 🔗 카테고리 연관관계 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 🔗 상품 이미지 연관관계 (OneToMany)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    // 등록일 자동 설정 (엔티티 저장 시 자동 설정)
    @PrePersist
    public void prePersist()
    {
        this.createdAt = LocalDateTime.now();
    }

    // 상품 진열 여부
    @Column(nullable = false)
    private boolean visible = true;

    // 신상품 여부 (NEW 뱃지 표시용)
    @Column(nullable = false)
    private boolean newProduct = false;

    // 출시일 (형식: yyyy-MM-dd)
    @Column
    private LocalDate releaseDate;

    // 태그 (콤마로 구분된 키워드 문자열)
    @Column(length = 500)
    private String tags;
}
