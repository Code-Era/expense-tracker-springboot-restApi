package com.codeera.expensetracker.service.Expense;

import com.codeera.expensetracker.aop.LogAspect;
import com.codeera.expensetracker.dto.Expense.ExpenseRequestDto;
import com.codeera.expensetracker.dto.Expense.ExpenseResponseDto;
import com.codeera.expensetracker.entity.Category;
import com.codeera.expensetracker.entity.Expense;
import com.codeera.expensetracker.entity.User;
import com.codeera.expensetracker.exception.ExceptionUtil;
import com.codeera.expensetracker.mapper.ExpenseMapper;
import com.codeera.expensetracker.repository.CategoryRepository;
import com.codeera.expensetracker.repository.ExpenseRepository;
import com.codeera.expensetracker.service.Category.CategoryService;
import com.codeera.expensetracker.util.SessionUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {



    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto) {

        Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
                .orElseThrow(
                        () -> ExceptionUtil.throwResourceNotFound("Category",
                                String.valueOf(expenseRequestDto.getCategoryId()))
                );

        Expense  expenseEntity = ExpenseMapper.mapToExpenseEntity(expenseRequestDto);

        expenseEntity.setUser(sessionUtil.getCurrentUserOrThrow());
        expenseEntity.setCategory(category);


        try {
            Expense savedExpense = expenseRepository.save(expenseEntity);
            return ExpenseMapper.mapToExpenseResponseDto(savedExpense);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save expense: " + e.getMessage());
        }

    }


    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        User user = sessionUtil.getCurrentUserOrThrow();

       return  expenseRepository.findAllByUser(user)
                    .stream()
                    .map(ExpenseMapper::mapToExpenseResponseDto)
                    .sorted(Comparator.comparing(ExpenseResponseDto::getTitle))
                    .toList();
    }

    @Override
    public ExpenseResponseDto getExpenseByID(Long id) {

      Expense expense =  expenseRepository.findById(id).orElseThrow(
              () -> ExceptionUtil.throwResourceNotFound("Expenses",String.valueOf(id)
      ));
      String email = sessionUtil.getCurrentUserEmail();

      if (!expense.getUser().getEmail().equals(email)) {
            throw ExceptionUtil.throwAccessDenied("You are not authorized to view this expense");
        }

      return ExpenseMapper.mapToExpenseResponseDto(expense);
    }

    @Override
    public ExpenseResponseDto updateExpense(long id, ExpenseRequestDto expenseRequestDto) {

        Expense existingExpense =  expenseRepository.findById(id).orElseThrow(
                () -> ExceptionUtil.throwResourceNotFound("Expenses",String.valueOf(id))
        );

        String email = sessionUtil.getCurrentUserEmail();
        if (!existingExpense.getUser().getEmail().equals(email)) {
            throw ExceptionUtil.throwAccessDenied("You are not authorized to update this expense");
        }

        // Update only allowed fields
        existingExpense.setTitle(expenseRequestDto.getTitle());
        existingExpense.setAmount(expenseRequestDto.getAmount());
        //existingExpense.setCategory(expenseRequestDto.getCategory());
        existingExpense.setDescription(expenseRequestDto.getDescription());

        Expense updated = expenseRepository.save(existingExpense);
        return ExpenseMapper.mapToExpenseResponseDto(updated);

    }

    @Override
    public long deleteExpense(long id) {
        Expense expense =  expenseRepository.findById(id).orElseThrow(
                () -> ExceptionUtil.throwResourceNotFound("Expenses",String.valueOf(id))
        );
        String email =sessionUtil.getCurrentUserEmail();
        if (!expense.getUser().getEmail().equals(email)) {
            throw ExceptionUtil.throwAccessDenied("You are not authorized to delete this expense");
        }

        expenseRepository.deleteById(id);
        return id;
    }


    @Override
    public List<ExpenseResponseDto> getAllExpenses(int pageNum, int size, String sortField, String sortDir) {
        User user = sessionUtil.getCurrentUserOrThrow();

      PageRequest pageRequest =  PageRequest.of(pageNum , size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());

        Page<Expense> expenses = expenseRepository.findByUser(user, pageRequest);

       return expenses.stream()
               .map(ExpenseMapper::mapToExpenseResponseDto)
                .toList();

       // return List.of();
    }
}
