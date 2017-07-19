package com.expensesplitter.expense;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.expensesplitter.shared.json.View;
import com.expensesplitter.user.User;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Expense {

    private String id;
    private LocalDateTime dateTime;
    private String description;
    private double amount;
    private String comment;
    private User user;

    public Expense() {
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @JsonView(View.Summary.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(View.Summary.class)
    @Column(nullable=false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(columnDefinition = "VARCHAR(40)", nullable=false)
    @JsonView(View.Summary.class)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonView(View.Summary.class)
    @Column(nullable=false)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(columnDefinition = "VARCHAR(200)")
    @JsonView(View.Summary.class)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "user_Id")
    @JsonView(View.Summary.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Expense)) {
            return false;
        }
        Expense expense = (Expense) obj;
        return expense.getId().equals(this.id);
    }
}
