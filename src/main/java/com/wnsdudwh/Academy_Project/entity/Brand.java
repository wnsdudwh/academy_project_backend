package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "brand")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand
{
    // 브랜드의 고유 ID값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 브랜드의 이름 (예: Fender, Ibanez, Strandberg* 등)
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // 브랜드 로고 URL (선택)
    @Column(length = 255)
    private String logoUrl;
}
