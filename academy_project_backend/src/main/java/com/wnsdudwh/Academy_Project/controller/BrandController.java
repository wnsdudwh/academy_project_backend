package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.BrandResponseDTO;
import com.wnsdudwh.Academy_Project.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController
{
    private final BrandService brandService;

    @GetMapping
    public List<BrandResponseDTO> getAllBrands()
    {
        return brandService.getAllBrands();
    }

}
