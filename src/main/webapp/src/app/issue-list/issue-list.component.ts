import {Component, Input, OnInit} from '@angular/core';
import {Project} from "../project";
import {ProjectService} from "../project.service";
import {Issue} from "../issue";
import {IssueService} from "../issue.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit {

  @Input()
  projectId: number;
  issues: Issue[];

  isFullCreate: boolean;
  newIssue: Issue;

  constructor(
    private projectService: ProjectService,
    private issueService: IssueService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.getIssues();
  }

  private getIssues(): void {
    this.projectService.getIssues({id: this.projectId} as Project)
      .subscribe(issues => this.issues = issues);
  }

  add(issueName: string): void {
    this.issueService.createIssue({name: issueName, projectId: this.projectId} as Issue)
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
