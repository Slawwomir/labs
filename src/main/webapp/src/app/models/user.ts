import {Role} from "./role";

export class User {
  id: number;
  username: string;
  role: Role;

  constructor(id: number, name: string, role: Role) {
    this.id = id;
    this.username = name;
    this.role = role;
  }
}
