package com.wnsdudwh.Academy_Project.controller;

import com.wnsdudwh.Academy_Project.dto.CategoryResponseDTO;
import com.wnsdudwh.Academy_Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories()
    {
        return categoryService.getAllCategories();
    }
}
