import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Role} from "../models/role";
import {authResult} from "../models/authResult";
import {Router} from "@angular/router";
import {User} from "../models/user";
import {UserService} from "./user.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient,
              private userService: UserService,
              private router: Router) {
  }

  public login(username: string, password: string) {
    return this.httpClient.post('/rest/authentication', {username, password})
      .pipe(map(this.setSession))
  }

  public logout(): void {
    localStorage.removeItem('auth_result');
    this.router.navigate(["login"]);
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('auth_result') != null;
  }

  public isUserInRole(role: Role): boolean {
    return this.getAuthResult()
      .roles.findIndex(r => r == role) != -1;
  }

  private getAuthResult() {
    return JSON.parse(localStorage.getItem('auth_result')) as authResult;
  }

  public changePassword(password: string) {
    return this.httpClient.put('/rest/authentication/password', {password})
      .pipe(map(this.setSession));
  }

  public getRoles() {
    return this.httpClient.get<Role[]>('/rest/authentication/roles');
  }

  public getLoggedInUser(): Observable<User> {
    return this.userService.getUser(this.getAuthResult().id);
  }

  private setSession(authResult): void {
    if (authResult && authResult.token) {
      localStorage.setItem('auth_result', JSON.stringify(authResult));
    }
  }

}
