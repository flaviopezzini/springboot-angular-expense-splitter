package com.expensesplitter.expense;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.expensesplitter.user.User;

public interface ExpenseRepository extends PagingAndSortingRepository<Expense, String> {
    @Query("select e from Expense e where e.user=:user and e.dateTime between :startDate and :endDate order by e.description")
    List<Expense> filterByUserAndDateRangeOrderByDescriptionAsc(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    // coalesce is used in case it returns null if no records match the where clause
    @Query("select coalesce(sum(e.amount), 0) from Expense e where e.user=:user and e.dateTime between :startDate and :endDate")
    Double sumByUserAndDateRange(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);

}