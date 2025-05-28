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

    // 조회 및 set~~ 분리 /  공통 조회 및 설정 유틸
    private Object[] getBrandAndCategory(Long brandId, Long categoryId)
    {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("해당 브랜드가 존재하지 않습니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));

        return new Object[]{brand, category};
    }

    private void applyDtoToProduct(Product product, ProductSaveRequestDTO dto, Brand brand, Category category)
    {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.isDiscount());
        product.setDiscountRate(dto.getDiscountRate());
        product.setPointRate(dto.getPointRate());
        product.setShippingFee(dto.getShippingFee());
        product.setStockTotal(dto.getStockTotal());
        product.setStatus(Status.valueOf(dto.getStatus())); // enum으로 관리 변경해서 valufOf 추가
        product.setShortDescription(dto.getShortDescription());
        product.setBrand(brand);
        product.setCategory(category);
        product.setVisible(dto.isVisible());
        product.setNewProduct(dto.isNewProduct());
        product.setReleaseDate(dto.getReleaseDate());
        product.setTags(dto.getTags());
    }

    // 이미지 저장 함수
    private void saveImages(ProductSaveRequestDTO dto, String productCode)
    {
        String uploadDir = "D:/upload/products/" + productCode;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        try
        {
            // 썸네일 저장
            MultipartFile thumb = dto.getThumbnail();
            thumb.transferTo(new File(uploadDir + "/" + thumb.getOriginalFilename()));

            // 서브 이미지 저장
            if (dto.getSubImages() != null)
            {
                for (MultipartFile img : dto.getSubImages()) {
                    img.transferTo(new File(uploadDir + "/" + img.getOriginalFilename()));
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("이미지 저장 중 오류 발생", e);
        }
    }


    @Override
    @Transactional
    public Long registerProduct(ProductSaveRequestDTO dto)
    {
        // 📌 브랜드, 카테고리 조회
        Object[] bc = getBrandAndCategory(dto.getBrandId(), dto.getCategoryId());
        Brand brand = (Brand) bc[0];
        Category category = (Category) bc[1];

        // 📌 Product 엔티티 생성
        Product product = Product.builder()
                .productCode(dto.getProductCode())
                .thumbnailUrl("/upload/products/" + dto.getProductCode() + "/" + dto.getThumbnail().getOriginalFilename())
                .viewCount(0)
                .soldCount(0)
                .build();

        applyDtoToProduct(product, dto, brand, category);
        productRepository.save(product); // 🔐 FK 미리 필요

        // ✅ 옵션 저장
        if (dto.getOptionList() != null && !dto.getOptionList().isEmpty())
        {
            for (ProductOptionSaveDTO optionDTO : dto.getOptionList())
            {
                ProductOption option = ProductOption.builder()
                        .optionName(optionDTO.getOptionName())
                        .optionType(optionDTO.getOptionType())
                        .additionalPrice(optionDTO.getAdditionalPrice())
                        .stock(optionDTO.getStock())
                        .soldOut(optionDTO.isSoldOut())
                        .product(product)
                        .build();

                productOptionRepository.save(option);
            }
        }

        if (dto.getOptionList() != null)
        {
            for (ProductOptionSaveDTO optionDTO : dto.getOptionList()) {
                ProductOption option = ProductOption.builder()
                        .optionName(optionDTO.getOptionName())
                        .optionType(optionDTO.getOptionType())
                        .additionalPrice(optionDTO.getAdditionalPrice())
                        .stock(optionDTO.getStock())
                        .soldOut(optionDTO.isSoldOut())
                        .product(product)
                        .build();
                productOptionRepository.save(option);
            }
        }

        if (dto.getSubImages() != null) {
            for (MultipartFile img : dto.getSubImages()) {
                String fileName = img.getOriginalFilename();
                ProductImage pi = ProductImage.builder()
                        .imageUrl("/upload/products/" + dto.getProductCode() + "/" + fileName)
                        .product(product)
                        .build();
                product.getImageList().add(pi);
            }
        }

        saveImages(dto, dto.getProductCode());

        return product.getId();
    }

    public List<ProductResponseDTO> getAllProducts()
    {
        return productRepository.findAll().stream()
                .map(product ->
                {
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
                            .status(product.getStatus().name()) // enum ~ .name()
                            .thumbnailUrl(product.getThumbnailUrl())
                            .shortDescription(product.getShortDescription())
                            .brandName(product.getBrand().getName())
                            .categoryName(product.getCategory().getName())
                            .discountPrice(discountPrice) // ✅ 여기에 세팅!
                            .visible(product.isVisible())
                            .newProduct(product.isNewProduct())
                            .releaseDate(product.getReleaseDate())
                            .tags(product.getTags())
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional
    public Long updateProduct(Long id, ProductSaveRequestDTO dto)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        Object[] bc = getBrandAndCategory(dto.getBrandId(), dto.getCategoryId());
        applyDtoToProduct(product, dto, (Brand) bc[0], (Category) bc[1]);

        String uploadDir = "D:/upload/products/" + dto.getProductCode();
        new File(uploadDir).mkdirs();

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
        return product.getId();
    }

    @Override
    @Transactional
    public void updateProductVisibility(Long id, boolean visible)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품 없음"));
        product.setVisible(visible);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void softDeleteProduct(Long id)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품 없음"));
        product.setStatus(Status.UNAVAILABLE); // enum 사용 중
        product.setVisible(false);
        productRepository.save(product);
    }

    @Override
    public void updateProductStatus(Long id, Status status)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품 없음"));
        product.setStatus(status);
        productRepository.save(product);
    }

    //통합으로 인한 주석 추후 삭제요망.
//    @Override
//    @Transactional
//    public Long updateProduct(Long id, ProductSaveRequestDTO dto)
//    {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
//
//        Object[] bc = getBrandAndCategory(dto.getBrandId(), dto.getCategoryId());
//        applyDtoToProduct(product, dto, (Brand) bc[0], (Category) bc[1]);
//
//        return product.getId();
//    }
}
