import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import 'rxjs/Rx';

@Injectable()
export class UtilService {

  constructor(private router: Router) {}

  isNumber(aString): boolean {
    return /^\d+$/.test(aString);
  }

  retrieveDatePortionAsStringWithTimeZone(date: Date): string {
    return new Date(date.getTime() - (date.getTimezoneOffset() * 60000)).toISOString().substring(0, 10);
  }

  // formats the date in yyyy-MM-dd format
  formatParamDate(date: Date): string {
    let format = '00';
    let month = (date.getMonth() + 1);
    let day = date.getDate();
    return date.getFullYear() + '-' +
      (format + month).slice(-format.length) + '-' +
      (format + day).slice(-format.length);
  }

  // formats the date in MM/dd/yyyy format
  formatUIDate(date: Date): string {
    let format = '00';
    let month = (date.getMonth() + 1);
    let day = date.getDate();
    return (format + month).slice(-format.length) + '/' +
      (format + day).slice(-format.length) + '/' +
      date.getFullYear();
  }

  getDateInLocalTimezone(date: Date): Date {
    return new Date(date.getTime() - (date.getTimezoneOffset() * 60000));
  }

  convertUIDateToParamDate(date: string): string {
    let parts = date.split('/');
    if (parts.length !== 3) {
      return null;
    }
    let month: number = parseInt(parts[0], 10) - 1;
    let day: number = parseInt(parts[1], 10);
    let year: number = parseInt(parts[2], 10);
    let retDate = new Date(year, month, day);
    return this.formatParamDate(retDate);
  }

  // valid format is MM/dd/yyyy
  isValidDate(date: string): boolean {
    try {
      let parts = date.split('/');
      if (parts.length !== 3) {
        return false;
      }
      let monthStr = parts[0];
      if (!this.isNumber(monthStr)) {
        return false;
      }
      let month = parseInt(monthStr, 10);
      if (month < 1 || month > 12) {
        return false;
      }
      let dayStr = parts[1];
      if (!this.isNumber(dayStr)) {
        return false;
      }
      let day = parseInt(dayStr, 10);
      if (day < 1 || day > 31) {
        return false;
      }
      // a zero to make it 1-index
      let daysPerMonth = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
      if (day > daysPerMonth[month]) {
        return false;
      }
      let yearStr = parts[2];
      if (!this.isNumber(yearStr)) {
        return false;
      }
      let year = parseInt(yearStr, 10);
    }
    catch (e) {
      return false;
    }

    return true;
  }
  // redirects when token expired
  handleError(error) {
    if (error._body.indexOf('Token has expired') !== -1) {
      this.router.navigate(['/tokenExpired']);
      return false;
    }
  }

  saveCookie(name, value, days) {
    let expires = '';
    if (days) {
        let date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = '; expires=' + date.toUTCString();
    }
    document.cookie = name + '=' + value + expires + '; path=/';
  }

  getCookie(name): string {
    let nameEQ = name + '=';
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
  }

  deleteCookie(name) {
    this.saveCookie(name, '', -1);
  }
}
