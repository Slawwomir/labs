import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../shared/issue.service";
import {Issue} from "../../../../shared/issue";
import {Project} from "../../../../shared/project";
import {ProjectService} from "../../../../project.service";
import {User, UserService} from "../shared/user.service";
import {Auth} from "../../../../shared/utils/auth";
import {ValidationUtils} from "../../../../shared/utils/validationUtils";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

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
    private userService: UserService
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
    this.issueEdit.reporterId = Auth.getCurrentUser().id;
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
