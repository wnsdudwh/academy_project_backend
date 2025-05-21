package com.wnsdudwh.Academy_Project.service;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;

import java.util.List;

public interface ProductService
{
    Long registerProduct(ProductSaveRequestDTO dto);

    List<ProductResponseDTO> getAllProducts();
}
