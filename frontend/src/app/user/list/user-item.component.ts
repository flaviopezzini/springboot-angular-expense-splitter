import { AuthService } from '../../auth/auth.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'et-user-item',
  templateUrl: './user-item.component.html'
})
export class UserItemComponent implements OnInit {

  @Input() user: User;
  @Input() userId: number;

  private isAdmin: boolean = false;
  private isUserManager: boolean = false;
  private isRegularUser: boolean = false;
  private roles: string[] = [];

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.isAdmin = this.authService.userIsAdmin(this.user);
    this.isUserManager = this.authService.userIsUserManager(this.user);
    this.isRegularUser = this.authService.userIsRegularUser(this.user);
    let count = 0;
    if (this.isAdmin) {
      this.roles.push('Admin');
    }
    if (this.isUserManager) {
      this.roles.push('User Manager');
    }
    if (this.isRegularUser) {
      this.roles.push('Regular User');
    }
  }

}
