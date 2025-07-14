package com.codeera.expensetracker.controller;


import com.codeera.expensetracker.constant.ResponseConstants;
import com.codeera.expensetracker.dto.Category.CategoryRequestDto;
import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import com.codeera.expensetracker.payload.ApiResponse;
import com.codeera.expensetracker.payload.ResponseService;
import com.codeera.expensetracker.service.Category.CategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

   private final CategoryService    categoryService;

    //Authenticated
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory
                                    (@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto);

        return ResponseService.buildSuccessResponse(categoryResponseDto,
                HttpStatus.CREATED.value(),
                ResponseConstants.Resource_CREATED);
    }

//Authenticated
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategoryAllList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc")  String direction
    ) {
        /*return ResponseService.buildSuccessResponse(categoryService.getAllCategory(page, size, sortBy, direction),
                HttpStatus.OK.value(),"Category Fetched Successfully");*/
        return ResponseService.buildSuccessResponse(categoryService.getAllCategory(),
                HttpStatus.OK.value(),"Category Fetched Successfully");

    }

    //Authenticated (only owner)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryListByd(@Valid @PathVariable Long id) {

        return ResponseService.buildSuccessResponse(categoryService.getCategoryByID(id),
                HttpStatus.OK.value(),"Category Fetched Successfully");
    }

    //Authenticated (only owner)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@Valid @PathVariable Long id,
                                                                         @Valid @RequestBody
                                                                         CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto categoryRequestDto1 =  categoryService.updateCategory(id, categoryRequestDto);

        return ResponseService.buildSuccessResponse(categoryRequestDto1,
                HttpStatus.OK.value(),"Category updated Successfully");

    }
    //Authenticated (only owner)
    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponse<CategoryResponseDto>> deleteCategory(@Valid @PathVariable Long id) {
        Long categoryId =  categoryService.deleteCategory(id);
        CategoryResponseDto expenseResponseDto = new CategoryResponseDto();
        expenseResponseDto.setId(categoryId);
        return ResponseService.buildSuccessResponse(expenseResponseDto,
                HttpStatus.OK.value(),"Expense deleted Successfully");
    }

}
