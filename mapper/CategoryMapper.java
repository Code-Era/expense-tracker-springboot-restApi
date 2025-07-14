package com.codeera.expensetracker.mapper;

import com.codeera.expensetracker.dto.Category.CategoryRequestDto;
import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import com.codeera.expensetracker.entity.Category;

import java.time.LocalDate;

public class CategoryMapper {

    public static Category mapToCategoryEntity(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setDescription(categoryRequestDto.getDescription());
        category.setTitle(categoryRequestDto.getTitle());
        category.setCreatedAt(LocalDate.now());
        return category;
    }

    public static CategoryResponseDto mapToCategoryResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setDescription(category.getDescription());
        categoryResponseDto.setTitle(category.getTitle());
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setCreatedBy(category.getCreatedBy());
        return categoryResponseDto;
    }
}
