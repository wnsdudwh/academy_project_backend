package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.BrandResponseDTO;
import com.wnsdudwh.Academy_Project.repository.BrandRepository;
import com.wnsdudwh.Academy_Project.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService
{
    private final BrandRepository brandRepository;

    @Override
    public List<BrandResponseDTO> getAllBrands()
    {
        return brandRepository.findAll()
                .stream()
                .map(brand -> new BrandResponseDTO(brand.getId(), brand.getName()))
                .collect(Collectors.toList());
    }
}
