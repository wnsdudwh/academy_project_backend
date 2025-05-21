package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.Brand;
import com.wnsdudwh.Academy_Project.entity.Category;
import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.repository.BrandRepository;
import com.wnsdudwh.Academy_Project.repository.CategoryRepository;
import com.wnsdudwh.Academy_Project.repository.ProductRepository;
import com.wnsdudwh.Academy_Project.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long registerProduct(ProductSaveRequestDTO dto)
    {
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Product product = Product.builder()
                .productCode(dto.getProductCode())
                .name(dto.getName())
                .price(dto.getPrice())
                .discount(dto.isDiscount())
                .discountRate(dto.getDiscountRate())
                .pointRate(dto.getPointRate())
                .shippingFee(dto.getShippingFee())
                .stockTotal(dto.getStockTotal())
                .status(dto.getStatus())
                .thumbnailUrl(dto.getThumbnailUrl())
                .shortDescription(dto.getShortDescription())
                .viewCount(0)
                .soldCount(0)
                .brand(brand)
                .category(category)
                .build();

        productRepository.save(product);
        return product.getId();
    }

    public List<ProductResponseDTO> getAllProducts()
    {
        return productRepository.findAll().stream()
                .map(product -> {
                    int rawPrice = product.getPrice();
                    BigDecimal price = new BigDecimal(rawPrice);
                    BigDecimal discountRate = product.getDiscountRate();
                    BigDecimal hundred = new BigDecimal("100");
                    BigDecimal rate = BigDecimal.ONE.subtract(discountRate.divide(hundred));

                    int discountPrice = product.isDiscount()
                            ? price.multiply(rate).intValue()
                            : rawPrice;

                    System.out.println("상품명: " + product.getName());
                    System.out.println("discount: " + product.isDiscount());
                    System.out.println("할인율 (discountRate): " + discountRate);
                    System.out.println("계산된 rate: " + rate);
                    System.out.println("계산된 할인가 (discountPrice): " + discountPrice);


                    // ✅ 계산한 값을 빌더에 넣기
                    return ProductResponseDTO.builder()
                            .id(product.getId())
                            .productCode(product.getProductCode())
                            .name(product.getName())
                            .price(rawPrice)
                            .discount(product.isDiscount())
                            .discountRate(product.getDiscountRate())
                            .pointRate(product.getPointRate())
                            .shippingFee(product.getShippingFee())
                            .stockTotal(product.getStockTotal())
                            .status(product.getStatus())
                            .thumbnailUrl(product.getThumbnailUrl())
                            .shortDescription(product.getShortDescription())
                            .brandName(product.getBrand().getName())
                            .categoryName(product.getCategory().getName())
                            .discountPrice(discountPrice) // ✅ 여기에 세팅!
                            .build();
                })
                .toList();
    }
}
