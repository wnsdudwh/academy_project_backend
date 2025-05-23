package com.wnsdudwh.Academy_Project.service;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ProductService
{
    Long registerProduct(ProductSaveRequestDTO dto);

    List<ProductResponseDTO> getAllProducts();

    @Transactional
    Long registerProductWithImages(ProductSaveRequestDTO dto);

    Long updateProduct(Long id, ProductSaveRequestDTO dto);
}
