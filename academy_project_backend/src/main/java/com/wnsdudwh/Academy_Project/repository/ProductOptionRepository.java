package com.wnsdudwh.Academy_Project.repository;

import com.wnsdudwh.Academy_Project.entity.Product;
import com.wnsdudwh.Academy_Project.entity.ProductOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long>
{
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductOption o WHERE o.product = :product")
    void deleteByProduct(@Param("product") Product product);

    List<ProductOption> findByProduct(Product product);
}