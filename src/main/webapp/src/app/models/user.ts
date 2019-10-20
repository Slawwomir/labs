import {Role} from "./role";

export class User {
  id: number;
  username: string;
  roles: Role[];

  constructor(id: number, name: string, roles: Role[]) {
    this.id = id;
    this.username = name;
    this.roles = roles;
  }
}
