package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    // 중복 코드 방지용
    Boolean existsByProductCode(String productCode);
}
