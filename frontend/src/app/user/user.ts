import { Role } from './role';
export class User {
  constructor(public id: string,
              public username: string,
              public password: string,
              public active: boolean,
              public roles: Role[]
              ) {
  }
}
