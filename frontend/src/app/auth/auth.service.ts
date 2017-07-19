import { UtilService } from '../shared/util.service';
import { User } from '../user/user';
import { LoggedInUser } from './logged-in-user';
import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class AuthService {
  private LOGIN_REST_PATH = '//localhost:8080/api/auth/login';
  private SIGNUP_REST_PATH = '//localhost:8080/api/auth/signup';
  private loggedInUser: LoggedInUser = null;

  constructor(private http: Http,
    private router: Router,
    private utilService: UtilService) {}

   isUserManager(): boolean {
    return this.isLoggedIn() && this.loggedInUser.roles.indexOf('USER_MANAGER') !== -1;
   }

   isAdmin(): boolean {
     return this.isLoggedIn() != null && this.loggedInUser.roles.indexOf('ADMIN') !== -1;
   }

   isRegularUser(): boolean {
     return this.isLoggedIn() && this.loggedInUser.roles.indexOf('REGULAR_USER') !== -1;
   }

  login(username: string, password: string): Observable<LoggedInUser> {
    let params = {
        'username': username,
        'password': password
    };
    let headers = new Headers(
      { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' }
    );
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.LOGIN_REST_PATH, JSON.stringify(params), options)
      .map((response: Response) => response.json());
  }

  signUp(username: string, password: string): Observable<Response> {
    let params = {
      username: username,
      password: password
    };
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({ headers: headers });
    return this.http.put(this.SIGNUP_REST_PATH, JSON.stringify(params), options)
      .map((response: Response) => response.json());
  }

  logout() {
    this.loggedInUser = null;
    this.utilService.deleteCookie('loggedInUser');
    this.router.navigate(['/auth/login']);
  }

  isLoggedIn(): boolean {
    if (this.loggedInUser == null) {
      let stored = this.utilService.getCookie('loggedInUser');
      if (stored) {
        let obj: Object = JSON.parse(stored);
        this.loggedInUser = obj as LoggedInUser;
        return true;
      }
      return false;
    } else {
      return true;
    }
  }

  getLoggedInUser(): LoggedInUser {
    return this.loggedInUser;
  }

  setLoggedInUser(loggedInUser: LoggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  getAuthorizationHeader(): Headers {
    return new Headers({'X-Authorization': 'Bearer: ' + this.loggedInUser.token});
  }

  userIsAdmin(user: User): boolean {
    return this.userIsRole(user, 'ADMIN');
  }

  userIsUserManager(user: User): boolean {
    return this.userIsRole(user, 'USER_MANAGER');
  }

  userIsRegularUser(user: User): boolean {
    return this.userIsRole(user, 'REGULAR_USER');
  }

  private userIsRole(user: User, roleToFind: string): boolean {
     for (let role of user.roles) {
       if (role.role === roleToFind) {
         return true;
       }
     }
     return false;
  }
}
