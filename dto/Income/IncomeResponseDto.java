package com.codeera.expensetracker.dto.Income;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeResponseDto {

    private String title;


    private Double amount;


    private String category;

    private String description;


    private String date ;

    private String createdByEmail;

}
