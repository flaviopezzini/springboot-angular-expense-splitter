import { Expense } from './expense';
import { ExpenseService } from './expense.service';
import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'et-expense-item',
  templateUrl: './expense-item.component.html'
})
export class ExpenseItemComponent implements OnInit {

  @Input() expense: Expense;
  @Input() expenseId: number;

  constructor() { }

  ngOnInit() {
  }

}
