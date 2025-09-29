package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.config.auth.PrincipalDetails;
import com.wnsdudwh.Academy_Project.dto.AddressResponseDTO;
import com.wnsdudwh.Academy_Project.dto.AddressSaveRequestDTO;
import com.wnsdudwh.Academy_Project.dto.AddressUpdateRequestDTO;
import com.wnsdudwh.Academy_Project.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController
{
    private final AddressService addressService;

    // 현재 로그인한 사용자의 모든 주소록을 조회하는 API
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getMyAddresses(@AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        // 1. @AuthenticationPrincipal 어노테이션으로 현재 로그인한 사용자 정보를 가져옵니다.
        Long memberIdx = principalDetails.getMember().getIdx();

        // 2. Service를 호출하여 주소록 목록을 조회합니다.
        List<AddressResponseDTO> addresses = addressService.getAddressesByMemberIdx(memberIdx);

        // 3. 조회된 목록을 성공 상태(200 OK)와 함께 반환합니다.
        return ResponseEntity.ok(addresses);
    }

    // 새로운 주소록을 추가하는 API
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody AddressSaveRequestDTO requestDTO)
    {
        // 1. 현재 로그인한 사용자 정보를 가져옵니다.
        Long memberIdx = principalDetails.getMember().getIdx();

        // 2. Service를 호출하여 주소록을 추가합니다.
        AddressResponseDTO savedAddress =  addressService.addAddress(memberIdx, requestDTO);

        // 3. 주소록이 성공적으로 '생성'되었음을 알리는 상태(201 Created)와 함께 결과를 반환합니다.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    // 주소록 단건 수정 API
    @PutMapping("{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressUpdateRequestDTO requestDTO, @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        Long memberIdx = principalDetails.getMember().getIdx();
        AddressResponseDTO updateAddress = addressService.updateAddress(memberIdx, addressId, requestDTO);

        return ResponseEntity.ok(updateAddress);
    }

    // 주소록 단건 삭제 API
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId, @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        Long memberIdx = principalDetails.getMember().getIdx();
        addressService.deleteAddress(memberIdx, addressId);

        // 성공적으로 삭제되었지만, 본문에 보낼 데이터가 없으므로 204 No Content 응답을 보냅니다.
        return ResponseEntity.noContent().build();
    }
}
