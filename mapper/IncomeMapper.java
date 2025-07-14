package com.codeera.expensetracker.mapper;

import com.codeera.expensetracker.dto.Income.IncomeRequestDto;
import com.codeera.expensetracker.dto.Income.IncomeResponseDto;
import com.codeera.expensetracker.entity.Income;

import java.time.LocalDate;


public class IncomeMapper {

    public static Income mapToIncomeEntity(IncomeRequestDto incomeRequestDto) {
        Income income = new Income();
        income.setTitle(incomeRequestDto.getTitle());
        income.setAmount(incomeRequestDto.getAmount());
        income.setDate(LocalDate.now());
        income.setDescription(incomeRequestDto.getDescription());
        income.setCategory(incomeRequestDto.getCategory());
        return income;
    }

    public static IncomeResponseDto mapToIncomeResponseDto(Income income) {
        IncomeResponseDto incomeResponseDto = new IncomeResponseDto();
        incomeResponseDto.setTitle(income.getTitle());
        incomeResponseDto.setAmount(income.getAmount());
        incomeResponseDto.setCategory(income.getCategory());
        incomeResponseDto.setDescription(income.getDescription());
        incomeResponseDto.setDate(String.valueOf(income.getDate())); //income.getDate());
        incomeResponseDto.setCreatedByEmail(income.getUser().getEmail());
        return incomeResponseDto;
    }
}
