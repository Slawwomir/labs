import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private httpClient: HttpClient
  ) {
  }

  getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>('/rest/users')
      .pipe(
        map(respone => respone["users"])
      )
  }

  getUser(id: number): Observable<User> {
    return this.httpClient.get<User>(`rest/users/${id}`)
  }

  updateUser(user: User): Observable<User> {
    return this.httpClient.put<User>('/rest/users', user);
  }
}
