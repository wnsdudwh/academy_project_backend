package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 옵션 이름 (예: Red, Black)
    @Column(nullable = false)
    private String optionName;

    // 옵션 타입 (예: 색상, 사이즈)
    @Column(nullable = false)
    private String optionType;

    // 추가 가격
    @Column(nullable = false)
    private int additionalPrice;

    // 재고 수량
    @Column(nullable = false)
    private int stock;

    // 품절 여부
    @Column(nullable = false)
    private boolean soldOut;

    // 연관 상품 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
