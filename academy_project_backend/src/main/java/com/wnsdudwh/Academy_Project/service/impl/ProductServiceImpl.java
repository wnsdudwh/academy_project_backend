package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.ProductOptionSaveDTO;
import com.wnsdudwh.Academy_Project.dto.ProductResponseDTO;
import com.wnsdudwh.Academy_Project.dto.ProductSaveRequestDTO;
import com.wnsdudwh.Academy_Project.entity.*;
import com.wnsdudwh.Academy_Project.repository.BrandRepository;
import com.wnsdudwh.Academy_Project.repository.CategoryRepository;
import com.wnsdudwh.Academy_Project.repository.ProductOptionRepository;
import com.wnsdudwh.Academy_Project.repository.ProductRepository;
import com.wnsdudwh.Academy_Project.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductOptionRepository productOptionRepository;

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

        // 옵션 리스트 저장
        if (dto.getOptions() != null && dto.getOptions().isEmpty())
        {
            for (ProductOptionSaveDTO optionDTO : dto.getOptions())
            {
                ProductOption option = ProductOption.builder()
                        .optionName(optionDTO.getOptionName())
                        .optionType(optionDTO.getOptionType())
                        .additionalPrice(optionDTO.getAdditionalPrice())
                        .stock(optionDTO.getStock())
                        .soldOut(optionDTO.isSoldOut())
                        .product(product) // FK 연관 ☆
                        .build();

                // repository 생성 후 저장
                productOptionRepository.save(option);
            }
        }
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

    @Override
    public Long registerProductWithImages(ProductSaveRequestDTO dto)
    {
        // 상품 저장
        Brand brand = brandRepository.findById(dto.getBrandId()).orElseThrow();
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

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
                .shortDescription(dto.getShortDescription())
                .brand(brand)
                .category(category)
                .thumbnailUrl("/upload/products/" + dto.getProductCode() + "/" + dto.getThumbnail().getOriginalFilename())
                .viewCount(0)
                .soldCount(0)
                .build();
        
        productRepository.save(product);
        
        // 이미지 저장
        String uploadDir = "D:/upload/products/" + dto.getProductCode();
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

// 📌 이미지 객체 생성 및 product에 추가
        if (dto.getSubImages() != null)
        {
            for (MultipartFile img : dto.getSubImages())
            {
                String fileName = img.getOriginalFilename();

                // 이미지 엔티티 생성
                ProductImage pi = ProductImage.builder()
                        .imageUrl("/upload/products/" + dto.getProductCode() + "/" + fileName)
                        .product(product) // 연관관계 설정
                        .build();

                product.getImageList().add(pi); // 양방향 연관관계 연결
            }
        }

        try
        {
            // 썸네일 저장
            MultipartFile thumb = dto.getThumbnail();
            thumb.transferTo(new File(uploadDir + "/" + thumb.getOriginalFilename()));

            // 서브 이미지 저장
            if (dto.getSubImages() != null)
            {
                for (MultipartFile img : dto.getSubImages())
                {
                    img.transferTo(new File(uploadDir + "/" + img.getOriginalFilename()));
                }

            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("이미지 저장 중 오류 발생", e);
        }

        return product.getId();
    }

    @Override
    public void updateProductWithImages(Long id, ProductSaveRequestDTO dto)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("해당 브랜드가 존재하지 않습니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));

        //  기존 값 갱신
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.isDiscount());
        product.setDiscountRate(dto.getDiscountRate());
        product.setPointRate(dto.getPointRate());
        product.setShippingFee(dto.getShippingFee());
        product.setStockTotal(dto.getStockTotal());
        product.setStatus(dto.getStatus());
        product.setShortDescription(dto.getShortDescription());
        product.setBrand(brand);
        product.setCategory(category);

        // 이미지가 넘어오면 새 이미지로 덮어쓰기
        String uploadDir = "D:/upload/products/" + dto.getProductCode();
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        try
        {
            if (dto.getThumbnail() != null)
            {
                MultipartFile thumb = dto.getThumbnail();
                thumb.transferTo(new File(uploadDir + "/" + thumb.getOriginalFilename()));
                product.setThumbnailUrl("/upload/products/" + product.getProductCode() + "/" + thumb.getOriginalFilename());
            }

            if (dto.getSubImages() != null && !dto.getSubImages().isEmpty())
            {
                // 기존 이미지 리스트 초기화 or 삭제하고 새로 추가
                product.getImageList().clear();

                for (MultipartFile img : dto.getSubImages())
                {
                    String fileName = img.getOriginalFilename();
                    img.transferTo(new File(uploadDir + "/" + fileName));

                    ProductImage pi = ProductImage.builder()
                            .imageUrl("/upload/products/" + product.getProductCode() + "/" + fileName)
                            .product(product)
                            .build();

                    product.getImageList().add(pi);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("이미지 저장 중 오류", e);
        }

        productRepository.save(product);
    }

    @Override
    @Transactional
    public Long updateProduct(Long id, ProductSaveRequestDTO dto)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("해당 브랜드가 존재하지 않습니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));

        //  기존 값 갱신
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.isDiscount());
        product.setDiscountRate(dto.getDiscountRate());
        product.setPointRate(dto.getPointRate());
        product.setShippingFee(dto.getShippingFee());
        product.setStockTotal(dto.getStockTotal());
        product.setStatus(dto.getStatus());
        product.setShortDescription(dto.getShortDescription());
        product.setBrand(brand);
        product.setCategory(category);

        return product.getId();
    }
}
