package com.wnsdudwh.Academy_Project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressUpdateRequestDTO
{
    private String name; // 배송지명

    private String recipient; // 받는 분

    private String address; // 주소

    private String detailAddress; // 상세주소

    private String zipCode; // 우편번호

    private String phoneNumber; // 연락처

    @JsonProperty("isDefault")
    private boolean isDefault; // 기본 배송지 여부
}
