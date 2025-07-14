package com.codeera.expensetracker.controller;

import com.codeera.expensetracker.constant.ExpenseConstant;
import com.codeera.expensetracker.dto.Expense.ExpenseRequestDto;
import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.payload.ApiResponse;
import com.codeera.expensetracker.payload.ResponseService;
import com.codeera.expensetracker.service.Expense.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    //Authenticated
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> createExpense
                                    (@Valid @RequestBody ExpenseRequestDto expenseRequestDto) {
        ExpenseResponseDto expenseResponseDto = expenseService.createExpense(expenseRequestDto);

        return ResponseService.buildSuccessResponse(expenseResponseDto,
                HttpStatus.CREATED.value(),
                ExpenseConstant.EXPENSE_CREATED);
    }

//Authenticated
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ExpenseResponseDto>>> getExpenseAllList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc")  String direction
    ) {
        return ResponseService.buildSuccessResponse(expenseService.getAllExpenses(page, size, sortBy, direction),
                HttpStatus.OK.value(),"Expense Fetched Successfully");

    }

    //Authenticated (only owner)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> getExpenseListByd(@Valid @PathVariable Long id) {

        return ResponseService.buildSuccessResponse(expenseService.getExpenseByID(id),
                HttpStatus.OK.value(),"Expense Fetched Successfully");
    }

    //Authenticated (only owner)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponseDto>> updateExpense(@Valid @PathVariable Long id, @Valid @RequestBody ExpenseRequestDto expenseRequestDto) {

        ExpenseResponseDto expenseResponseDto =  expenseService.updateExpense(id, expenseRequestDto);

        return ResponseService.buildSuccessResponse(expenseResponseDto,
                HttpStatus.OK.value(),"Expense updated Successfully");

    }
    //Authenticated (only owner)
    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponse<ExpenseResponseDto>> deleteExpense(@Valid @PathVariable Long id) {
        Long expenseId =  expenseService.deleteExpense(id);
        ExpenseResponseDto expenseResponseDto = new ExpenseResponseDto();
        expenseResponseDto.setId(expenseId);
        return ResponseService.buildSuccessResponse(expenseResponseDto,
                HttpStatus.OK.value(),"Expense deleted Successfully");
    }

}
