package com.codeera.expensetracker.mapper;

import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import com.codeera.expensetracker.dto.Expense.ExpenseRequestDto;
import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.entity.Expense;

import java.time.LocalDate;

public class ExpenseMapper {

    public static Expense mapToExpenseEntity(ExpenseRequestDto expenseRequestDto) {
        Expense expense= new Expense();
        expense.setTitle(expenseRequestDto.getTitle());
        expense.setAmount(expenseRequestDto.getAmount());
        expense.setDate(LocalDate.now());
        expense.setDescription(expenseRequestDto.getDescription());
        return expense;
    }

    public static ExpenseResponseDto mapToExpenseResponseDto(Expense expense) {
        ExpenseResponseDto expenseResponseDto = new ExpenseResponseDto();
        expenseResponseDto.setId(expense.getId());
        expenseResponseDto.setTitle(expense.getTitle());
        expenseResponseDto.setAmount(expense.getAmount());
        CategoryResponseDto categoryDto = new CategoryResponseDto();

        categoryDto.setId(expense.getCategory().getId());
        categoryDto.setTitle(expense.getCategory().getTitle());
        categoryDto.setDescription(expense.getCategory().getDescription());

        expenseResponseDto.setCategoryResponseDto(categoryDto);
        expenseResponseDto.setDescription(expense.getDescription());
      //  expenseResponseDto.setDate(expense.getDate().toString());
        expenseResponseDto.setCreatedByEmail(expense.getUser().getEmail());
        return expenseResponseDto;
    }
}
