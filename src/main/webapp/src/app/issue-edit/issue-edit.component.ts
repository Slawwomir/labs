import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../issue.service";
import {Issue} from "../issue";
import {Project} from "../project";
import {ProjectService} from "../project.service";

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

  constructor(
    private issueService: IssueService,
    private projectService: ProjectService
  ) {
  }

  ngOnInit() {
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

  save(): void {
    if(this.issue.id) {
      this.issueService.updateIssue(this.issue)
        .subscribe(issue => {
          this.issue = issue;
          this.onSave.emit(issue);
        });
    } else {
      this.issueService.createIssue(this.issue)
        .subscribe(issue => {
          this.issue = issue;
          this.onSave.emit(issue);
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
