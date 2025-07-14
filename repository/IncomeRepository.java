package com.codeera.expensetracker.repository;

import com.codeera.expensetracker.entity.Income;
import com.codeera.expensetracker.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository  extends JpaRepository<Income, Long> {

    boolean existsByTitleAndUser(String title, User user);

    List<Income> findAllByUser(User user);

    @Query("Select COALESCE(SUM(i.amount), 0)  from Income i where i.user = user")
    Double getTotalIncomeByUser(User user);

    @Query("select sum(e.amount) from Income e")
    Double getTotalIncomeForAllUser();
}
