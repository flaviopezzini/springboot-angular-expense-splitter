import { LoginComponent } from './auth/login.component';
import { ExpenseEditComponent } from './expense/edit/expense-edit.component';
import { ExpenseListComponent } from './expense/list/expense-list.component';
import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { UserEditComponent } from './user/edit/user-edit.component';
import { UserListComponent } from './user/list/user-list.component';

import { AuthGuard } from './auth/auth-guard';
import { EditProfileComponent } from './auth/edit-profile.component';
import { RegularUserGuard } from './auth/regular-user-guard';
import { SignUpComponent } from './auth/signup.component';
import { UserManagerGuard } from './auth/user-manager-guard';
import { NotFoundComponent } from './shared/not-found.component';
import { TokenExpiredComponent } from './shared/token-expired.component';

const appRoutes: Routes = [
    { path: '',
      component: HomeComponent,
      canActivate: [ AuthGuard]
    },
    { path: 'auth/login',
      component: LoginComponent
    },
    { path: 'user/signup',
      component: SignUpComponent
    },
    { path: 'user/editProfile',
      component: EditProfileComponent,
      canActivate: [ AuthGuard]
    },
    { path: 'expense',
      component: ExpenseListComponent,
      canActivate: [ RegularUserGuard]
    },
    { path: 'expense/new',
      component: ExpenseEditComponent,
      canActivate: [ RegularUserGuard]
    },
    { path: 'expense/:id/edit',
      component: ExpenseEditComponent,
      canActivate: [ RegularUserGuard]
    },
    { path: 'user',
      component: UserListComponent,
      canActivate: [ UserManagerGuard ]
    },
    { path: 'user/new',
      component: UserEditComponent,
      canActivate: [ UserManagerGuard ]
    },
    { path: 'user/:id/edit',
      component: UserEditComponent,
      canActivate: [ UserManagerGuard ]
    },
    { path: 'notFound',
      component: NotFoundComponent
    },
    { path: 'tokenExpired',
      component: TokenExpiredComponent
    },
    { path: '**', redirectTo: 'notFound', pathMatch: 'full' }
];

export const appRouterProviders: any[] = [];

export const routing: ModuleWithProviders =
  RouterModule.forRoot(appRoutes);
