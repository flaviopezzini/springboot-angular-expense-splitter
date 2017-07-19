package com.expensesplitter.expense;

import java.io.Serializable;
import java.util.List;

import com.expensesplitter.shared.json.View;
import com.fasterxml.jackson.annotation.JsonView;

public class WeeklyReport implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Expense> expenses;
    private double totalExpenses;
    private double dailyAverage;
    
    @JsonView(View.Summary.class)
    public List<Expense> getExpenses() {
        return expenses;
    }
    
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
    
    @JsonView(View.Summary.class)
    public double getTotalExpenses() {
        return totalExpenses;
    }
    
    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
    
    @JsonView(View.Summary.class)
    public double getDailyAverage() {
        return dailyAverage;
    }
    
    public void setDailyAverage(double dailyAverage) {
        this.dailyAverage = dailyAverage;
    }
}
