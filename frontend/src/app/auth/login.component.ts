import { UtilService } from '../shared/util.service';
import { AuthService } from './auth.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormArray, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'et-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  private loginForm: FormGroup;
  private errorMessage: string = '';
  constructor(private authService: AuthService,
              private utilService: UtilService,
              private formBuilder: FormBuilder,
              private router: Router) {
  }

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    let username: string = '';
    let password: string = '';

    this.loginForm = this.formBuilder.group({
      username : [username, Validators.required],
      password: [password, Validators.required]
    });
  }

  onSubmit() {
    this.errorMessage = '';
    this.authService.login(this.loginForm.value.username, this.loginForm.value.password)
      .subscribe(data => {
        this.authService.setLoggedInUser(data);
        const ONE_WEEK = 7;
        this.utilService.saveCookie('loggedInUser', JSON.stringify(data), ONE_WEEK);
        this.router.navigate(['']);
      }, error => {
        this.errorMessage = 'Login failed';
        this.utilService.handleError(error);
      });
  }
}
