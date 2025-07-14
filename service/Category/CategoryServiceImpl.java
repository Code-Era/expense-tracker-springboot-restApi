package com.codeera.expensetracker.service.Category;

import com.codeera.expensetracker.dto.Category.CategoryRequestDto;
import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import com.codeera.expensetracker.entity.Category;
import com.codeera.expensetracker.exception.ExceptionUtil;
import com.codeera.expensetracker.mapper.CategoryMapper;
import com.codeera.expensetracker.repository.CategoryRepository;
import com.codeera.expensetracker.repository.ExpenseRepository;
import com.codeera.expensetracker.util.SessionUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private final SessionUtil sessionUtil;

    private final CategoryRepository categoryRepository;

    private final ExpenseRepository expenseRepositorye;


    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {

      boolean ifFound = categoryRepository.existsByTitleAndCreatedBy(categoryRequestDto.getTitle(),
              sessionUtil.getCurrentUserEmail()) ;
      if(ifFound) {
         throw  ExceptionUtil.duplicate("Category", categoryRequestDto.getTitle());
      }

         Category category = CategoryMapper.mapToCategoryEntity(categoryRequestDto);

         category.setCreatedBy(sessionUtil.getCurrentUserEmail());


        Category categorySaved = categoryRepository.save(category);
         if(categorySaved.getId() > 0) {
             return CategoryMapper.mapToCategoryResponseDto(categorySaved);
         }

        return null;
    }

    @Override
    public List<CategoryResponseDto> getAllCategory() {
       // String email = sessionUtil.getCurrentUserEmail();

       List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryMapper :: mapToCategoryResponseDto)
                .collect(Collectors.toList());

    }

    @Override
    public CategoryResponseDto getCategoryByID(Long id) {

      Category category =  categoryRepository.findById(id)
                .orElseThrow(
                        () ->  ExceptionUtil.throwResourceNotFound("Category", String.valueOf(id))
                );

        return CategoryMapper.mapToCategoryResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> ExceptionUtil.throwResourceNotFound("Category", String.valueOf(id))
                );

        category.setTitle(categoryRequestDto.getTitle());
        category.setDescription(categoryRequestDto.getDescription());
        category.setCreatedBy(sessionUtil.getCurrentUserEmail());
        category.setCreatedAt(LocalDate.now());

        Category categorySaved = categoryRepository.save(category);

        return CategoryMapper.mapToCategoryResponseDto(category);


    }
    @Override
    public long deleteCategory(long id) {


        if (expenseRepositorye.existsByCategoryId(id))
            throw  ExceptionUtil.throwBadRequest("Category is in use and cannot be deleted");


        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> ExceptionUtil.throwResourceNotFound("Category", String.valueOf(id))
                );
        categoryRepository.deleteById(id);
        return id;
    }


    @Override
    public List<CategoryResponseDto> getAllCategory(int page, int size, String sortBy, String direction) {
        return List.of();
    }
}
