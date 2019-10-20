import {Role} from "./role";

export class authResult {
  token: string;
  username: string;
  id: number;
  roles: Role[];
}
