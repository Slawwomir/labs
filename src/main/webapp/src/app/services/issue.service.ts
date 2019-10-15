import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Issue} from "../models/issue";

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private httpClient: HttpClient,
  ) {
  }

  getIssue(id: number): Observable<Issue> {
    return this.httpClient.get<Issue>(`rest/issues/${id}`, this.httpOptions);
  }

  createIssue(issue: Issue): Observable<Issue> {
    return this.httpClient.post<Issue>('rest/issues', issue);
  }

  getStatuses(): Observable<string[]> {
    return this.httpClient.get<string[]>('rest/issues/status', this.httpOptions);
  }

  getIssueTypes(): Observable<string[]> {
    return this.httpClient.get<string[]>('rest/issues/type', this.httpOptions);
  }

  removeIssue(issue: Issue): Observable<any> {
    return this.httpClient.delete(`rest/issues/${issue.id}`, this.httpOptions);
  }

  updateIssue(issueEdit: Issue): Observable<Issue> {
    return this.httpClient.put<Issue>('rest/issues', issueEdit, this.httpOptions);
  }
}

