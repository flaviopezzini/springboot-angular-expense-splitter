import { User } from '../user';
import { UserService } from '../user.service';
import { UtilService } from '../../shared/util.service';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'et-user-list',
    templateUrl: 'user-list.component.html'
})
export class UserListComponent implements OnInit {
  private users: User[] = [];
  private errorMessage: string = '';
  private successMessage: string = '';

  constructor(
    private userService: UserService,
    private utilService: UtilService) {
   }

  ngOnInit() {
    this.userService.getUsers()
      .subscribe(
        (data: User[]) => {
          this.users = data;
          this.userService.setUsers(data);
        }, error => {
          this.utilService.handleError(error);
        }
      );
  }

  deactivateUser(userId) {
    this.errorMessage = '';
    this.successMessage = '';
    this.userService.deactivateUser(userId)
      .subscribe(data => {
        let user = this.users.find(theUser => theUser.id === userId);
        user.active = false;
        this.successMessage = 'User deactivated successfully';
      }, error => {
        this.errorMessage = 'Error on the server.';
        this.utilService.handleError(error);
      });
  };

  activateUser(userId) {
    this.errorMessage = '';
    this.successMessage = '';
    this.userService.activateUser(userId)
      .subscribe(data => {
        let user = this.users.find(theUser => theUser.id === userId);
        user.active = true;
        this.successMessage = 'User activated successfully';
      }, error => {
        this.errorMessage = 'Error on the server.';
        this.utilService.handleError(error);
      });
  };

}
