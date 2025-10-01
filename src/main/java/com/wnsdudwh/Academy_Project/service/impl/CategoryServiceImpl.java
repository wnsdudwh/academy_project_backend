package com.wnsdudwh.Academy_Project.service.impl;

import com.wnsdudwh.Academy_Project.dto.CategoryResponseDTO;
import com.wnsdudwh.Academy_Project.repository.CategoryRepository;
import com.wnsdudwh.Academy_Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO> getAllCategories()
    {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName()))
                .toList();
    }
}
