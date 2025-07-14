package com.codeera.expensetracker.dto.Expense;

import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseResponseDto {

    private Long id;
    private String title;
    private Double amount;
    private CategoryResponseDto categoryResponseDto;
    private String description;

    private String createdByEmail;
}
