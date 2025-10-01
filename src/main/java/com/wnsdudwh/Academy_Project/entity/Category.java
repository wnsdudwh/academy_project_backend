package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category
{
    // 브랜드의 고유 ID값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리 이름 (예: 일렉기타, 이펙트 등)
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // 상위 카테고리 ID → 서브 카테고리 구성용 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    //🧠 상위 카테고리 설정은 선택사항이지만, 향후 "통기타 > 미니기타" 같은 구조를 원할 경우 대비해 넣어두는 게 좋아.

}
