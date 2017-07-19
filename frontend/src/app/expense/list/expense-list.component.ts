import { AuthService } from '../../auth/auth.service';
import { UtilService } from '../../shared/util.service';
import { Expense } from '../expense';
import { ExpenseService } from '../expense.service';
import { Component } from '@angular/core';

@Component({
    selector: 'et-expense-list',
    templateUrl: 'expense-list.component.html'
})
export class ExpenseListComponent {
  private expenses: Expense[] = [];

  private filterDate: string;
  private filterUsername: string;

  private errorMessage: string = '';
  private successMessage: string = '';

  private isAdmin: boolean = false;

  constructor(private expenseService: ExpenseService,
    private authService: AuthService,
    private utilService: UtilService) {
   }

  ngOnInit() {
    this.isAdmin = this.authService.isAdmin();
    let today = new Date();
    this.filterDate = this.utilService.formatUIDate(today);
    this.filterUsername = this.authService.getLoggedInUser().username;

    this.refreshList();
  }

  setExpenses(expenses: Expense[]) {
    this.expenses = expenses;
    this.expenseService.setExpenses(expenses);
    // adjust timezone on the list
    for (let expense of expenses) {
      let asDate = new Date(expense.dateTime);
      expense.dateTime = this.utilService.getDateInLocalTimezone(asDate);
    }
  }

  refreshList() {
    this.errorMessage = '';
    this.successMessage = '';
    if (!this.utilService.isValidDate(this.filterDate)) {
      this.errorMessage = 'Invalid date';
      return false;
    }
    if (this.isAdmin) {
       this.expenseService.getExpensesAdmin(this.filterDate, this.filterUsername)
      .subscribe(
        (data: Expense[]) => {
          this.setExpenses(data);
        }, error => {
          this.errorMessage = 'Error on server';
          this.utilService.handleError(error);
        }
      );
    } else {
    this.expenseService.getExpenses(this.filterDate)
      .subscribe(
        (data: Expense[]) => {
          this.setExpenses(data);
        }, error => {
          this.errorMessage = 'Error on server';
          this.utilService.handleError(error);
        }
      );
    }
  }

  deleteExpense(expenseId: string) {
    this.errorMessage = '';
    this.successMessage = '';
    this.expenseService.deleteExpense(expenseId)
      .subscribe(data => {
        const index = this.expenses.findIndex(expense => expense.id === expenseId);
        this.expenses.splice(index, 1);
        this.expenseService.setExpenses(this.expenses);
        this.successMessage = 'Expense deleted successfully';
    }, error =>  {
      this.utilService.handleError(error);
    });
  }

  ngOnDestroy() {
  }

}
