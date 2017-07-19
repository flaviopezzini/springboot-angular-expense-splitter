export class LoggedInUser {
  constructor(public token: string,
              public refreshToken: string,
              public username: string,
              public roles: string
              ) {
  }
}
