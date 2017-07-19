import { AuthService } from '../auth/auth.service';
import { User } from '../user/user';
import { UserService } from '../user/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'et-home',
    templateUrl: 'home.component.html'
})
export class HomeComponent implements OnInit {

  username: string = '';
  isAdmin: boolean = false;
  isUserManager: boolean = false;
  isRegularUser: boolean = false;

  constructor(private authService: AuthService,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit() {
    this.username = this.authService.getLoggedInUser().username;
    this.isAdmin = this.authService.isAdmin();
    this.isUserManager = this.authService.isUserManager();
    this.isRegularUser = this.authService.isRegularUser();
  }

  editProfile() {
    this.router.navigate(['/user/editProfile']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['auth/login']);
  }
}
