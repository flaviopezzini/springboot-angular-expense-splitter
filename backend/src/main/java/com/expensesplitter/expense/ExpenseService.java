package com.expensesplitter.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensesplitter.user.User;
import com.expensesplitter.user.UserService;

@Service
public class ExpenseService {
    
    private static final LocalTime START_OF_DAY = LocalTime.of(0, 0, 0);
    private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserService userService;
    
    public double calculateDailyAverage(
            LocalDate startDate, 
            LocalDate endDate,
            double totalExpenses) {
        long dateDifference = ChronoUnit.DAYS.between(startDate, endDate);
        return totalExpenses / dateDifference;
    }
    
    public List<Expense> filterByUserAndDateRangeOrderByDescriptionAsc(
            String username, 
            LocalDate startDate, 
            LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, START_OF_DAY);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, END_OF_DAY);
        User user = this.userService.findByUsername(username);
        return expenseRepository.filterByUserAndDateRangeOrderByDescriptionAsc(
                user, startDateTime, endDateTime);
    }
    
    public Double sumByUserAndDateRange(
            String username, 
            LocalDate startDate, 
            LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, START_OF_DAY);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, END_OF_DAY);
        User user = this.userService.findByUsername(username);
        return expenseRepository.sumByUserAndDateRange(user, startDateTime, endDateTime);
    }
    
    public Expense save(Expense record) {
        return expenseRepository.save(record);
    }
    
    public void delete(String id) {
        expenseRepository.delete(id);
    }
}
