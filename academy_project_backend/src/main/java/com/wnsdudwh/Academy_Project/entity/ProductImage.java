package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 이미지 경로 (ex. /upload/products/GT-001/image1.jpg)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
