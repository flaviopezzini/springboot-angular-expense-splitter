import { UtilService } from '../shared/util.service';
import { AuthService } from './auth.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'et-signup',
  templateUrl: './signup.component.html'
})
export class SignUpComponent implements OnInit {
  private signUpForm: FormGroup;
  private errorMessage: string = '';

  private done: boolean = false;

  constructor(private authService: AuthService,
              private utilService: UtilService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    let username: string = '';
    let password: string = '';
    let repeatPassword: string = '';

    this.signUpForm = this.formBuilder.group({
      username : [username, Validators.required],
      password: [password, Validators.required],
      repeatPassword: [repeatPassword, Validators.required]
    });
  }

  onSubmit(): boolean {
    this.errorMessage = '';
    if (this.signUpForm.value.password !== this.signUpForm.value.repeatPassword) {
      this.errorMessage = 'Password and Repeat password do not match';
      return false;
    }

    this.authService.signUp(this.signUpForm.value.username, this.signUpForm.value.password)
      .subscribe(data => {
        this.utilService.deleteCookie('loggedInUser');
        this.authService.setLoggedInUser(null);
        this.done = true;
      }, error => {
        this.errorMessage = error.message;
        this.utilService.handleError(error);
      });
  }
}
