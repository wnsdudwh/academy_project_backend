package com.wnsdudwh.Academy_Project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wnsdudwh.Academy_Project.entity.Address;
import com.wnsdudwh.Academy_Project.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressSaveRequestDTO
{
    private String name;
    private String recipient;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String phoneNumber;

    @JsonProperty("isDefault")
    private boolean isDefault;

    public Address toEntity(Member member)
    {
        Address newAddress = new Address();
        newAddress.setMember(member);
        newAddress.setName(this.name);
        newAddress.setRecipient(this.recipient);
        newAddress.setAddress(this.address);
        newAddress.setDetailAddress(this.detailAddress);
        newAddress.setZipCode(this.zipCode);
        newAddress.setPhoneNumber(this.phoneNumber);
        newAddress.setDefault(this.isDefault);
        return newAddress;
    }
}
