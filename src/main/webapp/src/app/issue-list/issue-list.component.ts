import {Component, Input, OnInit} from '@angular/core';
import {Project} from "../project";
import {ProjectService} from "../project.service";
import {Issue} from "../issue";
import {IssueService} from "../issue.service";

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit {

  @Input()
  projectId: number;
  issues: Issue[];

  constructor(
    private projectService: ProjectService,
    private issueService: IssueService
  ) {
  }

  ngOnInit() {
    this.projectService.getIssues({id: this.projectId} as Project)
      .subscribe(issues => this.issues = issues);
  }

  add(issueName: string) {
    this.issueService.createIssue({name: issueName, projectId: this.projectId} as Issue)
      .subscribe(issue => this.issues.push(issue));
  }
}
