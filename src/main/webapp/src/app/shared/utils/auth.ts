import {User} from "../../services/user.service";

export class Auth {
  static getCurrentUser(): User {
    return {id: 1, username: "Tytus"} as User;
  }
}
