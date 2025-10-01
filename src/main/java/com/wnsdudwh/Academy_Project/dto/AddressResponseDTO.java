package com.wnsdudwh.Academy_Project.dto;

import com.wnsdudwh.Academy_Project.entity.Address;
import lombok.Getter;

@Getter
public class AddressResponseDTO
{
    private final Long id;
    private final String name;
    private final String recipient;
    private final String address;
    private final String detailAddress;
    private final String zipCode;
    private final String phoneNumber;
    private final boolean isDefault;

    public AddressResponseDTO(Address address)
    {
        this.id = address.getId();
        this.name = address.getName();
        this.recipient = address.getRecipient();
        this.address = address.getAddress();
        this.detailAddress = address.getDetailAddress();
        this.zipCode = address.getZipCode();
        this.phoneNumber = address.getPhoneNumber();
        this.isDefault = address.isDefault();
    }
}
