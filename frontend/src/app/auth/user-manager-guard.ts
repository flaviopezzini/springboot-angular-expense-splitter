import { AuthGuard } from './auth-guard';
import { Injectable, Injector } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable()
export class UserManagerGuard extends AuthGuard {

  constructor(injector: Injector) {
    super(injector);
  }

   canActivate(): boolean {
     if (super.canActivate()) {
       if (this.authService.isUserManager() || this.authService.isAdmin()) {
         return true;
       } else {
         this.router.navigate(['/notFound']);
         return false;
       }
     } else {
       return false;
     }
  }
}
