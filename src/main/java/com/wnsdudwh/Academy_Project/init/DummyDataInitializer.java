package com.wnsdudwh.Academy_Project.init;

import com.wnsdudwh.Academy_Project.entity.Brand;
import com.wnsdudwh.Academy_Project.entity.Category;
import com.wnsdudwh.Academy_Project.repository.BrandRepository;
import com.wnsdudwh.Academy_Project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements ApplicationRunner
{
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args)
    {
        // 🧩 브랜드 더미 데이터
        createBrand("Fender");
        createBrand("Ibanez");
        createBrand("Ernie Ball");
        createBrand("Strandberg");
        createBrand("Music Man");

        // 🧩 카테고리 더미 데이터
        createCategory("일렉기타");
        createCategory("어쿠스틱기타");
        createCategory("베이스");
        createCategory("이펙터");
        createCategory("앰프");
        createCategory("스트링");
        createCategory("피크");
        createCategory("기타악세서리");
    }

    private void createBrand(String name) 
    {
        if (!brandRepository.existsByName(name))
        {
            brandRepository.save(Brand.builder()
                            .name(name)
                            .logoUrl(null)  //로고는 추후 추가
                            .build());
        }
    }

    private void createCategory(String name)
    {
        if (!categoryRepository.existsByName(name))
        {
            categoryRepository.save(Category.builder()
                            .name(name)
                            .parent(null)   // 서브카테고리는 지금은 제외
                            .build());

        }
//
    }
}
