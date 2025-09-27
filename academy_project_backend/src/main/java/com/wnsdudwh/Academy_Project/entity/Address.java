package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name; // 배송지 명

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 설정
    @JoinColumn(name = "member_id")
    @JsonIgnore // 순환 참조 방지를 위해 추가
    private Member member;

    private String recipient; // 받는 분

    private String address; // 주소

    private String detailAddress; // 상세주소

    private String zipCode; // 우편번호

    private String phoneNumber; // 연락처

    private boolean isDefault; // 기본 배송지 여부
}
