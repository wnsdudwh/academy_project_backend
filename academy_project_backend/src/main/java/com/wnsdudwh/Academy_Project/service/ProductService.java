package com.wnsdudwh.Academy_Project.service;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Status;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ProductService
{
    Long registerProduct(ProductSaveRequestDTO dto);

    List<ProductResponseDTO> getAllProducts();

    Long updateProduct(Long id, ProductSaveRequestDTO dto);

    void updateProductVisibility(Long id, boolean visible);

    void softDeleteProduct(Long id);

    void updateProductStatus(Long id, Status status);

    ProductResponseDTO getProductById(Long id);


//    @Transactional
//    Long registerProductWithImages(ProductSaveRequestDTO dto);    추가 후 기존 register와 통합

//    Long updateProductWithImages(Long id, ProductSaveRequestDTO dto); //추가 후 기존 update와 통합
}
