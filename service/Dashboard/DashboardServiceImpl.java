package com.codeera.expensetracker.service.Dashboard;

import com.codeera.expensetracker.aop.LogAspect;
import com.codeera.expensetracker.aop.LoggerAspect;
import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.dto.Summary.AdminDashboardDto;
import com.codeera.expensetracker.dto.Summary.DashboardSummaryDto;
import com.codeera.expensetracker.dto.Summary.MonthlyStatDto;
import com.codeera.expensetracker.entity.Expense;
import com.codeera.expensetracker.entity.Income;
import com.codeera.expensetracker.entity.User;
import com.codeera.expensetracker.mapper.ExpenseMapper;
import com.codeera.expensetracker.repository.ExpenseRepository;
import com.codeera.expensetracker.repository.IncomeRepository;
import com.codeera.expensetracker.repository.UserRepository;
import com.codeera.expensetracker.util.SessionUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final SessionUtil sessionUtil;

    private final ExpenseRepository expenseRepository;

    private final IncomeRepository incomeRepository;

    private final UserRepository userRepository;


    @Override
    public DashboardSummaryDto getDashboardSummary() {

        User user = sessionUtil.getCurrentUserOrThrow();

        double dbExpense = expenseRepository.getTotalExpenseByUser(user);
        double dbIncome =  incomeRepository.getTotalIncomeByUser(user);

        DashboardSummaryDto     dashboardSummaryDto = new DashboardSummaryDto();
        dashboardSummaryDto.setTotalExpense(dbExpense);
        dashboardSummaryDto.setTotalIncome(dbIncome);
        dashboardSummaryDto.setNetBalance(dbIncome - dbExpense);
        return dashboardSummaryDto;
    }

    @Override
    public List<MonthlyStatDto> getMonthlySummary() {
        User user = sessionUtil.getCurrentUserOrThrow();

        List<MonthlyStatDto> ms = new ArrayList<>();

        List<Expense> dbExpense = expenseRepository.findAllByUser(user);
        List<Income> dbIncome =  incomeRepository.findAllByUser(user);

        Map<Integer, Double> incomeMap = dbIncome.stream()
                .collect(Collectors.groupingBy(
                        in -> in.getDate().getMonthValue(),
                        Collectors.summingDouble(Income::getAmount)
                ));
        Map<Integer, Double> exMap =dbExpense.stream()
                .collect(Collectors. groupingBy(
                        ex->ex.getDate().getMonthValue(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        for(int i = 1; i <= 12; i++) {
            MonthlyStatDto monthlyStatDto = new MonthlyStatDto();
            monthlyStatDto.setMonth(Month.of(i).name());
            monthlyStatDto.setIncome(incomeMap.getOrDefault(i, 0.0));
            monthlyStatDto.setExpense(exMap.getOrDefault(i, 0.0));
            ms.add(monthlyStatDto);
        }
        return ms;
    }

    @LogAspect
    @Override
    public List<ExpenseResponseDto> getTopThreeExpense() {
        User user = sessionUtil.getCurrentUserOrThrow();

        return expenseRepository.findTop3ByUserOrderByAmountDesc(user)
                .stream()
                .map(ExpenseMapper::mapToExpenseResponseDto)
                .collect(Collectors.toList());

    }

    /**
     * {
     *   "totalUsers": 56,
     *   "totalIncome": 235000,
     *   "totalExpense": 189000,
     *   "netBalance": 46000
     * }
     * @return
     */
    @Override
    public AdminDashboardDto getAdminDashboard() {

        AdminDashboardDto adminDashboardDto = new AdminDashboardDto();

        long totalUsers= userRepository.countUsersByRoleName("ROLE_USER");
        adminDashboardDto.setTotalUsers(totalUsers);


        Double dbIncome =  incomeRepository.getTotalIncomeForAllUser();
        Double dbExpense = expenseRepository.getTotalExpenseByAllUser();


        adminDashboardDto.setTotalIncome(dbIncome);
        adminDashboardDto.setTotalExpense(dbExpense);
        adminDashboardDto.setNetBalance(dbIncome - dbExpense);
        return adminDashboardDto;
    }
}
