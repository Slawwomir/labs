import {Injectable} from '@angular/core';
import {Project} from "../models/project";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Issue} from "../models/issue";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private httpClient: HttpClient,
  ) {
  }

  getProjects(): Observable<Project[]> {
    return this.httpClient.get<Project[]>(
      'rest/projects', {
        params: new HttpParams().set("size", "-1")
      }).pipe(map(response => response["projects"]));
  }

  getProject(id: number): Observable<Project> {
    return this.httpClient.get<Project>(`rest/projects/${id}`);
  }

  updateProject(project: Project): Observable<Project> {
    return this.httpClient.put<Project>('rest/projects', project, this.httpOptions);
  }

  createProject(project: Project): Observable<Project> {
    return this.httpClient.post<Project>('rest/projects', project, this.httpOptions);
  }

  removeProject(id: number): Observable<any> {
    return this.httpClient.delete(`rest/projects/${id}`);
  }

  getIssuesForProject(project: Project, filters): Observable<Issue[]> {
    return this.httpClient.get(
      `rest/projects/${project.id}/issues`, {
        params: filters
      }).pipe(map(response => response["issues"]))
  }

  getIssues(filters): Observable<Issue[]> {
    filters = Object.entries(filters).filter(([key, value]) => value).reduce<any>((httpParams, [key, value]) => httpParams.set(key, value), new HttpParams());
    return this.httpClient.get(
      'rest/issues', {
        params: filters
      }).pipe(map(response => response["issues"]));
  }
}
