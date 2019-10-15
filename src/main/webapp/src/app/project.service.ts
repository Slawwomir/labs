import {Injectable} from '@angular/core';
import {Project} from "./shared/project";
import {Observable, of} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, map, tap} from "rxjs/operators";
import {Issue} from "./shared/issue";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:8080/lab2-1.0-SNAPSHOT/rest';
  private projectsPath = '/projects';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private httpClient: HttpClient,
  ) {
  }

  getProjects(): Observable<Project[]> {
    return this.httpClient.get<Project[]>(
      this.getProjectsUrl(), {
        params: new HttpParams().set("size", "-1")
      }).pipe(
      map(response => response["projects"]),
      catchError(this.handleError<any>('getProjects'))
    );
  }

  getProject(id: number): Observable<Project> {
    return this.httpClient.get<Project>(this.getProjectsUrl() + `/${id}`)
      .pipe(
        catchError(this.handleError<any>('getProject'))
      );
  }

  updateProject(project: Project): Observable<Project> {
    return this.httpClient.put<Project>(this.getProjectsUrl(), project, this.httpOptions);
  }

  createProject(project: Project): Observable<Project> {
    return this.httpClient.post<Project>(this.getProjectsUrl(), project, this.httpOptions);
  }

  removeProject(id: number): Observable<any> {
    return this.httpClient.delete(this.getProjectsUrl() + `/${id}`);
  }

  getIssues(project: Project, filters): Observable<Issue[]> {
    return this.httpClient.get(
      `${this.getProjectsUrl()}/${project.id}/issues`, {
        params: filters
      }).pipe(
      map(response => response["issues"])
    )
  }

  private getProjectsUrl(): string {
    return `${this.baseUrl}${this.projectsPath}`;
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
