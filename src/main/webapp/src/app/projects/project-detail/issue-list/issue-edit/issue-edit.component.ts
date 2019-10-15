import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../shared/issue.service";
import {Issue} from "../../../../shared/issue";
import {Project} from "../../../../shared/project";
import {ProjectService} from "../../../../project.service";
import {User, UserService} from "../shared/user.service";
import {Auth} from "../../../../shared/utils/auth";
import {ValidationUtils} from "../../../../shared/utils/validationUtils";

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
  users: User[];
  projects: Project[];
  errors: String[];

  constructor(
    private issueService: IssueService,
    private projectService: ProjectService,
    private userService: UserService
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

    if (!this.users) {
      this.getUsers();
    }
  }

  onSubmit(): void {
    if (this.issue.id) {
      this.updateIssue();
    } else {
      this.saveIssue();
    }
  }

  private saveIssue() {
    this.issue.reporterId = Auth.getCurrentUser().id;
    this.issueService.createIssue(this.issue)
      .subscribe(issue => {
        this.issue = issue;
        this.onSave.emit(issue);
      }, error => {
        this.errors = ValidationUtils.mapErrors(error);
      });
  }

  private updateIssue() {
    this.issueService.updateIssue(this.issue)
      .subscribe(issue => {
        this.issue = issue;
        this.onSave.emit(issue);
      }, error => {
        this.errors = ValidationUtils.mapErrors(error);
      });
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

  private getUsers() {
    this.userService.getUsers()
      .subscribe(users => this.users = users);
  }
}
