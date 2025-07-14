package com.codeera.expensetracker.service.Dashboard;


import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.dto.Summary.AdminDashboardDto;
import com.codeera.expensetracker.dto.Summary.DashboardSummaryDto;
import com.codeera.expensetracker.dto.Summary.MonthlyStatDto;

import java.util.List;

public interface DashboardService {

    DashboardSummaryDto getDashboardSummary();

    List<MonthlyStatDto> getMonthlySummary();

    List<ExpenseResponseDto> getTopThreeExpense();

    AdminDashboardDto getAdminDashboard();
}
