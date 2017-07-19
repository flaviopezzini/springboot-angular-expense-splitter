import { User } from '../user/user';
import { UserService } from '../user/user.service';
import { AuthService } from './auth.service';
import { UtilService } from '../shared/util.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'et-edit-profile',
  templateUrl: './edit-profile.component.html'
})
export class EditProfileComponent implements OnInit {
  private userForm: FormGroup;
  private errorMessage: string = '';
  private userId: string;
  private user: User = new User('', '', '', false, []);
  private done: boolean = false;

  constructor(private authService: AuthService,
              private userService: UserService,
              private utilService: UtilService,
              private formBuilder: FormBuilder,
              private router: Router) { }

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    let username = this.authService.getLoggedInUser().username;
    let oldPassword = '';
    let newPassword = '';
    this.userForm = this.formBuilder.group({
      username: [username, Validators.required],
      oldPassword: [oldPassword],
      newPassword: [newPassword]
    });
  }

  onSubmit() {
    this.errorMessage = '';
    let newPassword = this.userForm.value.newPassword;
    let oldPassword = this.userForm.value.oldPassword;

    if ((!newPassword && oldPassword) || (!oldPassword && newPassword)) {
      this.errorMessage = 'Please enter both passwords';
      return false;
    }

    this.user.username = this.userForm.value.username;
    this.user.password = newPassword;
    this.userService.updateProfile(this.user, oldPassword)
       .subscribe(data => {
          if (data.message) {
            this.errorMessage = data.message;
          } else {
            let newLoggedInUser = this.authService.getLoggedInUser();
            newLoggedInUser.username = data.username;
            this.authService.setLoggedInUser(newLoggedInUser);
            this.done = true;
          }
    }, error => {
      if (error._body.indexOf('Invalid old password') !== -1) {
       this.errorMessage = 'Invalid old password';
      } else {
        this.errorMessage = 'Error on server';
      }
       this.utilService.handleError(error);
    });
  }

}
