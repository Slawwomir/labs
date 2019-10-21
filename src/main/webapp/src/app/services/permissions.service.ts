import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Role} from "../models/role";
import {Observable} from "rxjs";
import {Permission} from "../models/permission";

@Injectable({
  providedIn: 'root'
})
export class PermissionsService {

  constructor(private httpClient: HttpClient) {
  }

  public getRoles(): Observable<Role[]> {
    return this.httpClient.get<Role[]>("/rest/permission/roles");
  }

  public getMethods(): Observable<String[]> {
    return this.httpClient.get<String[]>('/rest/permission/methods');
  }

  public getLevels(): Observable<String[]> {
    return this.httpClient.get<String[]>('/rest/permission/levels');
  }

  public getPermissions(): Observable<Permission[]> {
    return this.httpClient.get<Permission[]>('/rest/permission');
  }

  public addPermission(permission: Permission): Observable<Permission> {
    return this.httpClient.post<Permission>('/rest/permission', permission);
  }

  public removePermission(id: number) {
    return this.httpClient.delete(`/rest/permission/${id}`);
  }
}
