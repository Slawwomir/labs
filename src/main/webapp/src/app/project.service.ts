import {Injectable} from '@angular/core';
import {Project} from "./project";
import {Observable, of} from "rxjs";
import {MessageService} from "./message.service";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, map, tap} from "rxjs/operators";
import {Issue} from "./issue";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private baseUrl = 'http://localhost:8080/lab2-1.0-SNAPSHOT/rest';
  private projectsPath = '/project';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private httpClient: HttpClient,
    private messageService: MessageService
  ) {
  }

  getProjects(): Observable<Project[]> {
    this.messageService.add('ProjectService: fetched projects');
    return this.httpClient.get<Project[]>(this.getProjectsUrl(), {params: new HttpParams().set("size", "-1")})
      .pipe(
        map(response => response["projects"]),
        tap(_ => this.log('get projects')),
        catchError(this.handleError<any>('getProjects'))
      );
  }

  getProject(id: number): Observable<Project> {
    this.messageService.add(`ProjectService: fetched project id=${id}`);
    return this.httpClient.get<Project>(this.getProjectsUrl() + `/${id}`)
      .pipe(
        tap(_ => this.log(`get project id=${id}`)),
        catchError(this.handleError<any>('getProject'))
      );
  }

  updateProject(project: Project): Observable<Project> {
    return this.httpClient.put<Project>(this.getProjectsUrl(), project, this.httpOptions)
      .pipe(
        tap(_ => this.log(`update project id=${project.id}`))
      )
  }

  createProject(project: Project): Observable<Project> {
    return this.httpClient.post<Project>(this.getProjectsUrl(), project, this.httpOptions)
      .pipe(
        tap(_ => this.log(`create project name=${project.name}`))
      )
  }

  removeProject(id: number): Observable<any> {
    return this.httpClient.delete(this.getProjectsUrl() + `/${id}`)
      .pipe(
        tap(_ => this.log(`remove project id=${id}`)),
      )
  }

  getIssues(project: Project, filters): Observable<Issue[]> {
    return this.httpClient.get(`${this.getProjectsUrl()}/${project.id}/issues`, {params: filters})
      .pipe(
        map(response => response["issues"]),
        tap(_ => this.log(`get project issues id=${project.id}`)),
      )
  }

  private log(message: string) {
    this.messageService.add(`ProjectService: ${message}`);
  }

  private getProjectsUrl(): string {
    return `${this.baseUrl}${this.projectsPath}`;
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
