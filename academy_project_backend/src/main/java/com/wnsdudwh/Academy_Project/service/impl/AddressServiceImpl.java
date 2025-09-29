package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.AddressResponseDTO;
import com.wnsdudwh.Academy_Project.dto.AddressSaveRequestDTO;
import com.wnsdudwh.Academy_Project.dto.AddressUpdateRequestDTO;
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

    @Override
    @Transactional
    public AddressResponseDTO updateAddress(Long memberIdx, Long addressId, AddressUpdateRequestDTO requestDTO)
    {
        // 1. 수정할 주소를 DB에서 조회합니다.
        Address address =  addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 없습니다. id=" + addressId));

        // 2. [보안] 해당 주소가 현재 로그인한 사용자의 주소가 맞는지 확인합니다.
        if (!address.getMember().getIdx().equals(memberIdx))
        {
            throw new IllegalStateException("주소를 수정 할 권한이 없습니다.");
        }

        // 3. 만약 이 주소를 '기본 배송지'로 설정한다면, 기존의 기본 배송지는 해제합니다.
        if (requestDTO.isDefault())
        {
            addressRepository.findByMemberIdx(memberIdx).stream()
                    .filter(Address::isDefault) //  기존 기본 배송지를 찾아서
                    .findFirst()
                    .ifPresent(defaultAddress ->
                    {
                        if (!defaultAddress.getId().equals(addressId)) // 자기 자신이 아닐 경우에만
                        {
                            defaultAddress.setDefault(false);
                        }
                    });
        }

        // 4. DTO의 내용으로 주소 정보(Entity)를 업데이트합니다.
        address.setName(requestDTO.getName());
        address.setRecipient(requestDTO.getRecipient());
        address.setAddress(requestDTO.getAddress());
        address.setDetailAddress(requestDTO.getDetailAddress());
        address.setZipCode(requestDTO.getZipCode());
        address.setPhoneNumber(requestDTO.getPhoneNumber());
        address.setDefault(requestDTO.isDefault());

        // 5. 변경된 내용을 ResponseDTO로 변환하여 반환합니다.
        return new AddressResponseDTO(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long memberIdx, Long addressId)
    {
        // 1. 삭제할 주소를 DB에서 조회합니다.
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 없습니다. id=" + addressId));

        // 2. [보안] 해당 주소가 현재 로그인한 사용자의 주소가 맞는지 확인합니다.
        if (!address.getMember().getIdx().equals(memberIdx))
        {
            throw new IllegalStateException("주소를 삭제 할 권한이 없습니다.");
        }

        // 3. 주소를 삭제합니다.
        addressRepository.delete(address);
    }
}
