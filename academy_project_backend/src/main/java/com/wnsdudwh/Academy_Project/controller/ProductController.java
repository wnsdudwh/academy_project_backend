package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;

    // 📌 상품 등록 API
    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductSaveRequestDTO dto)
    {
        Long saveId = productService.registerProduct(dto);
        return ResponseEntity.ok("상품 등록 완료 (ID : " + saveId + ")");
    }

    // 상품 전체 조회 API
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts()
    {
        List<ProductResponseDTO> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

}
