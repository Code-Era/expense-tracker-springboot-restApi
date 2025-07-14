package com.codeera.expensetracker.controller;

import com.codeera.expensetracker.dto.Category.CategoryResponseDto;
import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.dto.Summary.AdminDashboardDto;
import com.codeera.expensetracker.dto.Summary.DashboardSummaryDto;
import com.codeera.expensetracker.dto.Summary.MonthlyStatDto;
import com.codeera.expensetracker.payload.ApiResponse;
import com.codeera.expensetracker.payload.ResponseService;
import com.codeera.expensetracker.service.Dashboard.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/v1/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getDashboardSummary() {
        return ResponseService.buildSuccessResponse( dashboardService.getDashboardSummary(),
                HttpStatus.OK.value(), "Dashboard Fetched Successfully");
    }


    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyStatDto>>> getMonthlySummary() {
        return ResponseService.buildSuccessResponse( dashboardService.getMonthlySummary(),
                HttpStatus.OK.value(), "Dashboard Monthly Fetched Successfully");

    }




    @GetMapping("/top-expense")
    public ResponseEntity<ApiResponse<List<ExpenseResponseDto>>> getTopExpense() {
        return ResponseService.buildSuccessResponse( dashboardService.getTopThreeExpense(),
                HttpStatus.OK.value(), "Top Expense Fetched Successfully");
    }





    @GetMapping("/admin/overview")
    public ResponseEntity<ApiResponse<AdminDashboardDto>> getAdminDashboard() {
        return ResponseService.buildSuccessResponse( dashboardService.getAdminDashboard(),
                HttpStatus.OK.value(), "Admin Dashboard Fetched Successfully");

    }


}
