import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { LocationStrategy, HashLocationStrategy, APP_BASE_HREF } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AlertModule, DatepickerModule } from 'ng2-bootstrap';
import { routing, appRouterProviders } from './app.routing';
import { AppComponent } from './app.component';

import { HomeComponent } from './home/home.component';

import { LoginComponent } from './auth/login.component';
import { SignUpComponent } from './auth/signup.component';

import { EditProfileComponent } from './auth/edit-profile.component';

import { ExpenseEditComponent } from './expense/edit/expense-edit.component';
import { ExpenseItemComponent } from './expense/expense-item.component';
import { ExpenseListComponent } from './expense/list/expense-list.component';

import { UserEditComponent } from './user/edit/user-edit.component';
import { UserItemComponent } from './user/list/user-item.component';
import { UserListComponent } from './user/list/user-list.component';

import { NotFoundComponent } from './shared/not-found.component';

import { AuthService } from './auth/auth.service';
import { ExpenseService } from './expense/expense.service';
import { UserService } from './user/user.service';
import { UtilService } from './shared/util.service';

import { AuthGuard } from './auth/auth-guard';
import { RegularUserGuard } from './auth/regular-user-guard';
import { UserManagerGuard } from './auth/user-manager-guard';
import { TokenExpiredComponent } from './shared/token-expired.component';

@NgModule({
    declarations: [AppComponent,

                   HomeComponent,

                   LoginComponent,
                   SignUpComponent,

                   EditProfileComponent,

                   ExpenseEditComponent,
                   ExpenseItemComponent,
                   ExpenseListComponent,

                   UserEditComponent,
                   UserItemComponent,
                   UserListComponent,

                   NotFoundComponent,
                   TokenExpiredComponent
                 ],
    imports: [BrowserModule,
              FormsModule,
              ReactiveFormsModule,
              HttpModule,
              AlertModule.forRoot(),
              DatepickerModule.forRoot(),
              routing],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [
        ExpenseService,
        UserService,
        AuthService,
        UtilService,

        AuthGuard,
        RegularUserGuard,
        UserManagerGuard,

        appRouterProviders,
        [{provide: APP_BASE_HREF, useValue: '/'}],
        [{provide: LocationStrategy, useClass: HashLocationStrategy}]
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
