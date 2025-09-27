package com.wnsdudwh.Academy_Project.service;

import com.wnsdudwh.Academy_Project.dto.AddressResponseDTO;
import com.wnsdudwh.Academy_Project.dto.AddressSaveRequestDTO;

import java.util.List;

public interface AddressService
{
    // 현재 로그인한 사용자의 모든 주소록 조회
    List<AddressResponseDTO> getAddressesByMemberIdx(Long memberIdx);

    // 주소록 추가
    AddressResponseDTO addAddress(Long memberIdx, AddressSaveRequestDTO requestDTO);

    // (추후 구현) 주소록 수정
    // AddressResponseDTO updateAddress(Long addressId, AddressUpdateRequestDto requestDto);

    // (추후 구현) 주소록 삭제
    // void deleteAddress(Long addressId);
}
