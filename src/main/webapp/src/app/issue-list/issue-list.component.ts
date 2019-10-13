import {Component, Input, OnInit} from '@angular/core';
import {Project} from "../project";
import {ProjectService} from "../project.service";
import {Issue} from "../issue";
import {IssueService} from "../issue.service";
import {Location} from "@angular/common";
import {Auth} from "../utils/auth";

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit {

  @Input()
  projectId: number;
  issues: Issue[];
  statuses: string[];
  filterByStatus: String;

  isFullCreate: boolean;
  newIssue: Issue;

  constructor(
    private projectService: ProjectService,
    private issueService: IssueService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.filterByStatus = "";
    this.getIssues();
    this.getStatuses();
  }

  private getIssues(): void {
    const filters = (this.filterByStatus && this.filterByStatus != "null") ? {status: this.filterByStatus} : {};
    this.projectService.getIssues({id: this.projectId} as Project, filters)
      .subscribe(issues => this.issues = issues);
  }

  private getStatuses(): void {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  add(issueName: string): void {
    this.issueService.createIssue({
      name: issueName,
      projectId: this.projectId,
      reporterId: Auth.getCurrentUser().id,
      status: "OPEN",
      type: "TASK"
    } as Issue)
      .subscribe(issue => this.issues.push(issue));
  }

  fullCreate(issueName: string): void {
    this.isFullCreate = true;
    this.newIssue = {name: issueName, projectId: this.projectId} as Issue;
  }

  save(issue: Issue): void {
    this.isFullCreate = false;
    this.issues.push(issue);
  }

  remove(issue: Issue): void {
    this.issueService.removeIssue(issue)
      .subscribe(_ => this.getIssues());
  }

  goBack(): void {
    this.location.back();
  }
}
