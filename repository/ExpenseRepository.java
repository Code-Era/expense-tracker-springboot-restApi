package com.codeera.expensetracker.repository;

import com.codeera.expensetracker.entity.Expense;
import com.codeera.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUser(User user);

    List<Expense> findByUser(User user);
    Page<Expense> findByUser(User user, Pageable pageable);


     boolean existsByCategoryId(Long categoryId);

     @Query("select COALESCE(SUM(e.amount), 0) FROM Expense e where e.user = :user")
     Double getTotalExpenseByUser(User user);


     List<Expense> findTop3ByUserOrderByAmountDesc(User user);


    @Query("select COALESCE(SUM(e.amount), 0) FROM Expense e ")
    Double getTotalExpenseByAllUser();
}
