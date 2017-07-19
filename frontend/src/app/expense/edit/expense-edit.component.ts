import { Expense } from '../expense';
import { ExpenseService } from '../expense.service';
import { UtilService } from '../../shared/util.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { FormArray, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'et-expense-edit',
  templateUrl: './expense-edit.component.html'
})
export class ExpenseEditComponent implements OnInit, OnDestroy {
  private expenseForm: FormGroup;
  private expenseId: string;
  private expense: Expense;
  private subscription: Subscription;
  private isNew = true;
  private done = false;
  private errorMessage: string = '';

  constructor(private route: ActivatedRoute,
              private expenseService: ExpenseService,
              private utilService: UtilService,
              private formBuilder: FormBuilder,
              private router: Router) { }

  ngOnInit() {
    this.subscription = this.route.params.subscribe(
      (params: any) => {
        if (params.hasOwnProperty('id')) {
          this.isNew = false;
          this.expenseId = params['id'];
          this.expense = this.expenseService.getExpense(this.expenseId);
        } else {
          this.isNew = true;
          this.expense = new Expense(
            '',
            null,
            '',
            0,
            '',
            ''
          );
        }
        this.initForm();
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  initForm() {
    let description = '';
    let amount = 0;
    let comment: string = '';
    let workingDate: Date = new Date();
    if (!this.isNew) {
      workingDate = new Date(this.expense.dateTime);
      description = this.expense.description;
      amount = this.expense.amount;
      comment = this.expense.comment;
    }
    let dateStr: string = this.utilService.formatUIDate(workingDate);
    let timeStr: string = workingDate.toTimeString().substring(0, 8);
    
    this.expenseForm = this.formBuilder.group({
      date: [dateStr, Validators.required],
      time: [timeStr, Validators.required],
      description : [description, Validators.required],
      amount: [amount, Validators.required],
      comment: [comment, null]
    });
  }

  onSubmit() {
    this.errorMessage = '';
    if (!this.utilService.isValidDate(this.expenseForm.value.date)) {
      this.errorMessage = 'Invalid Date';
      return false;
    }
    let paramDate = this.utilService.convertUIDateToParamDate(this.expenseForm.value.date);
    this.expense.dateTime = new Date(paramDate + 'T' + this.expenseForm.value.time);
    this.expense.description = this.expenseForm.value.description;
    this.expense.amount = this.expenseForm.value.amount;
    this.expense.comment = this.expenseForm.value.comment;
    this.expenseService.saveExpense(this.expense)
      .subscribe(data => {
        this.done = true;
      }, error => {
        this.utilService.handleError(error);
      });
  }
}
