package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    // 1. 모든 Product를 찾을 때, 연관된 brand와 category를 함께 "즉시 로딩"
    @Query(" SELECT p FROM Product p JOIN FETCH p.brand JOIN FETCH p.category ")
    List<Product> findAllWithBrandAndCategory();

    // 2. ID로 Product를 찾을 때, 연관된 모든 상세 정보(브랜드, 카테고리, 이미지)를 "함께 로딩"
    @Query(" SELECT p FROM Product p " +
                    " LEFT JOIN FETCH p. brand " +
                    " LEFT JOIN FETCH p.category  " +
                    " LEFT JOIN FETCH p.imageList WHERE p.id = :id ")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);

    // 3. 상품 코드가 이미 존재하는지 확인 (중복 코드 방지용)
    Boolean existsByProductCode(String productCode);
}
