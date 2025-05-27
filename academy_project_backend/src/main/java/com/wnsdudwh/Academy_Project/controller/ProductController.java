package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.entity.ProductImage;
import com.wnsdudwh.Academy_Project.repository.ProductRepository;
import com.wnsdudwh.Academy_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
        dto.parseOptions(); // 파싱 수행
        Long saveId = productService.registerProduct(dto);
        System.out.println("썸네일 파일: " + dto.getThumbnail());
        System.out.println("썸네일 URL: " + dto.getThumbnailUrl()); // ← 여기 null이면 원인
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
    public ResponseEntity<String> updateProductWithImages(@PathVariable Long id, @ModelAttribute ProductSaveRequestDTO dto)
    {
        Long updatedId = productService.updateProductWithImages(id, dto);
        return ResponseEntity.ok("상품 수정 완료 (ID : " + updatedId + ")");
    }

    // 상품 상세 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") Long id)
    {
        // 📌 1. 상품 ID로 DB에서 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        // 상품 이미지 조회
        List<String> subImageUrls = product.getImageList()
                .stream()
                .map(ProductImage::getImageUrl)
                .toList();

        // 📌 2. 가격 관련 데이터 계산 (할인가 포함)
        int rawPrice = product.getPrice();
        BigDecimal price = new BigDecimal(rawPrice);
        BigDecimal discountRate = product.getDiscountRate();
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal rate = BigDecimal.ONE.subtract(discountRate.divide(hundred));

        // 📌 3. 할인 여부에 따라 최종 가격 계산
        int discountPrice = product.isDiscount()
                ? price.multiply(rate).intValue()
                : rawPrice;

        // 📌 4. DTO로 변환하여 프론트로 넘길 데이터 구성
        ProductResponseDTO dto = ProductResponseDTO.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .name(product.getName())
                .price(rawPrice)
                .discountRate(discountRate)
                .discountPrice(discountPrice)
                .discount(product.isDiscount())
                .pointRate(product.getPointRate())
                .shippingFee(product.getShippingFee())
                .stockTotal(product.getStockTotal())
                .status(product.getStatus())
                .thumbnailUrl(product.getThumbnailUrl())
                .shortDescription(product.getShortDescription())
                .brandName(product.getBrand().getName()) // 💡 연관관계에서 브랜드명 추출
                .categoryName(product.getCategory().getName()) // 💡 연관관계에서 카테고리명 추출
                .subImages(subImageUrls)
                .build();

        // 📌 5. 정상 응답 반환 (HTTP 200 OK + 상품 데이터)
        return ResponseEntity.ok(dto);
    }
}
