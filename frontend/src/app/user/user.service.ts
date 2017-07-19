import { AuthService } from '../auth/auth.service';
import { User } from './user';
import { LoggedInUser } from '../auth/logged-in-user';
import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions } from '@angular/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class UserService {
  private REST_PATH = '//localhost:8080/api/users';
  private users: User[] = [];

  constructor(private http: Http,
    private authService: AuthService) {

   }

  setUsers(users: User[]) {
    this.users = users;
  }

  getUsers(): Observable<User[]> {
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
     return this.http.get(this.REST_PATH, options)
      .map((response: Response) => response.json());
  }

  getUser(id: string): User {
    return this.users.find(user => user.id === id);
  }

  deactivateUser(userId: string): Observable<Boolean> {
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
    return this.http.put(this.REST_PATH + '/' + userId + '/deactivate', userId, options)
      .map((response: Response) => response.json());
  }

  activateUser(userId: string): Observable<Boolean> {
    let headers = this.authService.getAuthorizationHeader();
    let options = new RequestOptions({ headers: headers });
    return this.http.put(this.REST_PATH + '/' + userId + '/activate', userId, options)
      .map((response: Response) => response.json());
  }

  saveUser(user: User, isAdmin: boolean, isUserManager: boolean, isRegularUser: boolean ): Observable<User> {
    let userSave = {
      user: user,
      isAdminParameter: isAdmin,
      isUserManagerParameter: isUserManager,
      isRegularUserParameter: isRegularUser
    };
    let headers = this.authService.getAuthorizationHeader();
    headers.set('Content-Type', 'application/json');
    let options = new RequestOptions({ headers: headers });
    if (user.id) {
        return this.http.put(this.REST_PATH + '/' + user.id, JSON.stringify(userSave), options)
        .map((response: Response) => response.json());
    } else {
        return this.http.post(this.REST_PATH, JSON.stringify(userSave), options)
        .map((response: Response) => response.json());
    }
  }

  updateProfile(user: User, oldPassword: string) {
    let params = {
      user: user,
      oldPassword: oldPassword
    };
    let headers = this.authService.getAuthorizationHeader();
    headers.set('Content-Type', 'application/json');
    let options = new RequestOptions({ headers: headers });
    return this.http.put(this.REST_PATH + '/updateProfile', JSON.stringify(params), options)
      .map((response: Response) => response.json());
  }
}
