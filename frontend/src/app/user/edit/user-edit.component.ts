import { AuthService } from '../../auth/auth.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { UtilService } from '../../shared/util.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { FormArray, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'et-user-edit',
  templateUrl: './user-edit.component.html'
})
export class UserEditComponent implements OnInit, OnDestroy {
  private userForm: FormGroup;
  private isNew = true;
  private userId: string;
  private user: User;
  private done: boolean = false;
  private subscription: Subscription;

  private isAdmin: boolean = false;
  private isUserManager: boolean = false;
  private isRegularUser: boolean = false;

  private loggedInIsAdmin: boolean = false;
  private loggedInIsUserManager: boolean = false;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private authService: AuthService,
              private utilService: UtilService,
              private formBuilder: FormBuilder,
              private router: Router) { }

  ngOnInit() {
    this.loggedInIsAdmin = this.authService.isAdmin();
    this.loggedInIsUserManager = this.authService.isUserManager();

    this.subscription = this.route.params.subscribe(
      (params: any) => {
        if (params.hasOwnProperty('id')) {
          this.isNew = false;
          this.userId = params['id'];
          this.user = this.userService.getUser(this.userId);
          this.isAdmin = this.authService.userIsAdmin(this.user);
          this.isUserManager = this.authService.userIsUserManager(this.user);
          this.isRegularUser = this.authService.userIsRegularUser(this.user);
        } else {
          this.isNew = true;
          this.user = new User(
            '',
            '',
            '',
            false,
            []
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
    let username: string = '';
    let password: string = '';
    let active: boolean = true;

    if (!this.isNew) {
      username = this.user.username;
      password = this.user.password;
      active = this.user.active;
    }
    this.userForm = this.formBuilder.group({
      username: [username, Validators.required],
      password: [password],
      active: [active],
      isAdmin: [this.isAdmin],
      isUserManager: [this.isUserManager],
      isRegularUser: [this.isRegularUser]
    });
    if (this.isNew) {
      this.userForm.get('password').setValidators([Validators.required]);
    }
  }

  onSubmit() {
    this.user.username = this.userForm.value.username;
    this.user.password = this.userForm.value.password;
    this.user.active = this.userForm.value.active;

    this.userService.saveUser(
      this.user,
      this.userForm.value.isAdmin,
      this.userForm.value.isUserManager,
      this.userForm.value.isRegularUser)
      .subscribe(data => {
        this.done = true;
      }, error => {
        this.utilService.handleError(error);
      });
  }

}
