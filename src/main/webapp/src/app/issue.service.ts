import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MessageService} from "./message.service";
import {Observable} from "rxjs";
import {Issue} from "./issue";

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  private baseUrl = 'http://localhost:8080/lab2-1.0-SNAPSHOT/rest';
  private issuesPath = '/issue';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private httpClient: HttpClient,
    private messageService: MessageService
  ) {
  }

  getIssue(id: number): Observable<Issue> {
    return this.httpClient.get<Issue>(`${this.getIssuesUrl()}/${id}`,this.httpOptions);
  }

  createIssue(issue: Issue): Observable<Issue> {
    return this.httpClient.post<Issue>(this.getIssuesUrl(), issue);
  }

  getStatuses(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.getIssuesUrl()}/status`, this.httpOptions);
  }

  getIssueTypes(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.getIssuesUrl()}/type`, this.httpOptions);
  }

  private getIssuesUrl(): string {
    return `${this.baseUrl}${this.issuesPath}`;
  }

  updateIssue(issueEdit: Issue): Observable<Issue> {
    return this.httpClient.put<Issue>(this.getIssuesUrl(), issueEdit, this.httpOptions);
  }
}

