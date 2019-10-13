import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../issue.service";
import {Issue} from "../issue";
import {Project} from "../project";
import {ProjectService} from "../project.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-issue-edit',
  templateUrl: './issue-edit.component.html',
  styleUrls: ['./issue-edit.component.css']
})
export class IssueEditComponent implements OnInit {

  @Input()
  issue: Issue;

  @Output()
  onSave: EventEmitter<Issue> = new EventEmitter();

  statuses: string[];
  types: string[];
  projects: Project[];
  errors: string[];

  constructor(
    private issueService: IssueService,
    private projectService: ProjectService
  ) {
  }

  ngOnInit() {
    this.errors = [];
    this.issue = Object.assign({}, this.issue);
    if (!this.statuses) {
      this.getStatuses();
    }

    if (!this.types) {
      this.getIssueTypes();
    }

    if (!this.projects) {
      this.getProjects();
    }
  }

  onSubmit(): void {
    if(this.issue.id) {
      this.issueService.updateIssue(this.issue)
        .subscribe(issue => {
          this.issue = issue;
          this.onSave.emit(issue);
        }, error => {

        });
    } else {
      this.issueService.createIssue(this.issue)
        .subscribe(issue => {
          this.issue = issue;
          this.onSave.emit(issue);
        }, error => {
          if (error instanceof HttpErrorResponse) {
            const errorMessages = [];
            error.error.parameterViolations.forEach(err => {
              errorMessages[err.path.split(".")[2]] = err.message;
            });

            this.errors = errorMessages;
          }
        });
    }
  }

  private getStatuses(): void {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  private getIssueTypes() {
    this.issueService.getIssueTypes()
      .subscribe(issueTypes => this.types = issueTypes);
  }

  private getProjects() {
    this.projectService.getProjects()
      .subscribe(projects => this.projects = projects);
  }
}
