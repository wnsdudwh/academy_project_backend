package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.AddressResponseDTO;
import com.wnsdudwh.Academy_Project.dto.AddressSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Address;
import com.wnsdudwh.Academy_Project.entity.Member;
import com.wnsdudwh.Academy_Project.repository.AddressRepository;
import com.wnsdudwh.Academy_Project.repository.MemberRepository;
import com.wnsdudwh.Academy_Project.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService
{
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;    // Member를 조회하기 위해 추가
    
    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 성능 향상
    public List<AddressResponseDTO> getAddressesByMemberIdx(Long memberIdx)
    {
        return addressRepository.findByMemberIdx(memberIdx).stream()
                .map(AddressResponseDTO::new)   // Stream API를 사용해 Entity 리스트를 DTO 리스트로 변환
                .collect(Collectors.toList());
    }

    @Override
    @Transactional  // 데이터 변경이 있으므로 readOnly = false (기본값)
    public AddressResponseDTO addAddress(Long memberIdx, AddressSaveRequestDTO requestDTO)
    {
        // 1. DTO로 부터 주소를 추가할 회원을 찾습니다. 회원이 없으면 예외 발생.
        Member member =  memberRepository.findById(memberIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. idx=" + memberIdx));

        // 현재 등록된 주소 개수를 확인하는 로직
        List<Address> existingAddresses = addressRepository.findByMemberIdx(memberIdx);
        
        if (existingAddresses.size() >= 10)
        {
            throw new IllegalStateException("배송지는 최대 10개까지 등록할 수 있습니다.");
        }

        // 2. 만약 새로 추가하는 주소를 '기본 배송지'로 설정했다면, 기존에 있던 다른 기본 배송지를 해제
        if (requestDTO.isDefault())
        {
            addressRepository.findByMemberIdx(memberIdx).stream()
                    .filter(Address::isDefault)
                    .findFirst()
                    .ifPresent(defaultAddress -> defaultAddress.setDefault(false));
        }

        // 3. DTO를 Entity로 변환하여 저장합니다.
        Address savedAddress =  addressRepository.save(requestDTO.toEntity(member));

        // 4. 저장된 Entity를 ResponseDTO로 변환하여 반환합니다.
        return new AddressResponseDTO(savedAddress);
    }
}
