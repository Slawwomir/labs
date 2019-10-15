import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

export class User {
  id: number;
  username: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.username = name;
  }
}

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

  getCurrentUser(): Observable<User> {
    // authorization not implemented, returns a random user
    return this.getUsers().pipe(map(users => users[0]))
  }

  getUser(id: number) : Observable<User> {
    return this.httpClient.get<User>(`rest/users/${id}`)
  }
}
