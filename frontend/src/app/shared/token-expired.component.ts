import { AuthService } from '../auth/auth.service';
import { UtilService } from './util.service';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'et-token-expired',
    templateUrl: 'token-expired.component.html'
})
export class TokenExpiredComponent implements OnInit {

  constructor(
    private utilService: UtilService,
    private authService: AuthService) {
  }

  ngOnInit() {
    this.authService.setLoggedInUser(null);
    this.utilService.deleteCookie('loggedInUser');
  }
}
