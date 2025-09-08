package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.entity.ProductImage;
import com.wnsdudwh.Academy_Project.entity.Status;
import com.wnsdudwh.Academy_Project.repository.ProductRepository;
import com.wnsdudwh.Academy_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;
    private final ProductRepository productRepository;

    // 📌 상품 등록 API
    @PostMapping("/register")
    public ResponseEntity<String> registerProduct(@ModelAttribute ProductSaveRequestDTO dto)
    {
        Long saveId = productService.registerProduct(dto);
        System.out.println("썸네일 파일: " + dto.getThumbnail());
        System.out.println("썸네일 URL: " + dto.getThumbnailUrl());
        return ResponseEntity.ok("상품 등록 완료 (ID : " + saveId + ")");
    }

    // 상품 전체 조회 API
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts()
    {
        List<ProductResponseDTO> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // 이미지 수정+ 상품수정
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @ModelAttribute ProductSaveRequestDTO dto)
    {
        Long updatedId = productService.updateProduct(id, dto);
        return ResponseEntity.ok("상품 수정 완료 (ID : " + updatedId + ")");
    }

    // 상품 상세 조회 API 
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") Long id)
    {
        ProductResponseDTO dto = productService.getProductById(id);
        return ResponseEntity.ok(dto);
    }

    // 어드민 상품목록에서 원터치로 (눈 아이콘) 진열 및 숨김 처리할 API
    @PutMapping("{id}/visibility")
    public ResponseEntity<Void> updateProductVisibility(@PathVariable Long id, @RequestBody Map<String, Boolean> request)
    {
        boolean visible = request.get("visible");
        productService.updateProductVisibility(id, visible);
        return ResponseEntity.ok().build();
    }

    // 어드민 상품목록에서 관리 (상품상태 SELECT) 원터치 수정
    @PutMapping("{id}/status")
    public ResponseEntity<?> updateProductStatus(@PathVariable Long id, @RequestBody Map<String, String> request)
    {
        String status = request.get("status");
        productService.updateProductStatus(id, Status.valueOf(status)); //enum 사용
        return ResponseEntity.ok().build();
    }

    // 어드민 상품목록에서 삭제 (휴지통 아이콘) 클릭시 호출할 소프트 삭제 API
    @PutMapping("{id}/soft-delete")
    public ResponseEntity<Void> softDeleteProduct(@PathVariable Long id)
    {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
