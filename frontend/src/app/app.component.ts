import { Component, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'expense-splitter',
    template: '<router-outlet></router-outlet>',
    styleUrls: ['app.scss'],
    encapsulation: ViewEncapsulation.None
})
export class AppComponent {
    name = 'expense-splitter';
}
