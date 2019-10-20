import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../../../../../services/issue.service";
import {Issue} from "../../../../../models/issue";
import {Project} from "../../../../../models/project";
import {ProjectService} from "../../../../../services/project.service";
import {UserService} from "../../../../../services/user.service";
import {ValidationUtils} from "../../../../../shared/utils/validationUtils";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {AuthService} from "../../../../../services/auth.service";
import {User} from "../../../../../models/user";

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
  issueEdit: Issue;

  constructor(
    private router: Router,
    private issueService: IssueService,
    private projectService: ProjectService,
    private userService: UserService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.errors = [];
    this.issueEdit = Object.assign({}, this.issue);
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
    let issue: Observable<Issue>;
    if (this.issueEdit.id) {
      issue = this.updateIssue();
    } else {
      issue = this.saveIssue();
    }

    issue.subscribe(issue => {
      if (this.issue.projectId != issue.projectId) {
        this.router.navigate(['projects', issue.projectId])
      }
      this.issueEdit = issue;
      this.onSave.emit(issue);
    }, error => {
      this.errors = ValidationUtils.mapErrors(error);
    });

  }

  private saveIssue(): Observable<Issue> {
    this.issueEdit.reporterId = this.authService.getUserId();
    return this.issueService.createIssue(this.issueEdit)
  }

  private updateIssue(): Observable<Issue> {
    return this.issueService.updateIssue(this.issueEdit)
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
