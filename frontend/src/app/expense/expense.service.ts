import { AuthService } from '../auth/auth.service';
import { UtilService } from '../shared/util.service';
import { Expense } from './expense';
import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class ExpenseService {
  private REST_PATH = '//localhost:8080/api/expenses';

  private expenses: Expense[] = [];

  constructor(private http: Http,
    private authService: AuthService,
    private utilService: UtilService) {}

  // date comes in MM/dd/yyyy format
  getExpenses(filterDate: string) {
    let date = new Date(filterDate);
    let paramDate = this.utilService.formatParamDate(date);
    let url = this.REST_PATH + '/filter/byDateRange?startDate=' + paramDate + '&endDate=' + paramDate;
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
    return this.http.get(url, options)
      .map((response: Response) => response.json());
  }

  setExpenses(expenses: Expense[]) {
    this.expenses = expenses;
  }

  // date comes in MM/dd/yyyy format
  getExpensesAdmin(filterDate: string, filterUserName: string) {
    let date = new Date(filterDate);
    let paramDate = this.utilService.formatParamDate(date);
    let url = this.REST_PATH + '/admin/filter/byDateRange?startDate=' + paramDate +
        '&endDate=' + paramDate +
        '&username=' + filterUserName;
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
    return this.http.get(url, options)
      .map((response: Response) => response.json());
  }

  getExpense(id: string) {
    return this.expenses.find(expense => expense.id === id);
  }

  deleteExpense(expenseId: string) {
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
    return this.http.delete(this.REST_PATH + '/' + expenseId, options)
      .map((response: Response) => response.json());
  }

  saveExpense(expense: Expense) {
    let headers = this.authService.getAuthorizationHeader();
    headers.set('Content-Type', 'application/json');
    let options = new RequestOptions({ headers: headers });
    if (expense.id) {
        return this.http.put(this.REST_PATH + '/' + expense.id, JSON.stringify(expense), options)
        .map((response: Response) => response.json());
    } else {
        return this.http.post(this.REST_PATH, JSON.stringify(expense), options)
        .map((response: Response) => response.json());
    }
  }
}
