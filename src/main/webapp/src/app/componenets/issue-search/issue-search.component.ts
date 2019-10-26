import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user";
import {Project} from "../../models/project";
import {IssueService} from "../../services/issue.service";
import {ProjectService} from "../../services/project.service";
import {UserService} from "../../services/user.service";
import {Issue} from "../../models/issue";

@Component({
  selector: 'app-issue-search',
  templateUrl: './issue-search.component.html',
  styleUrls: ['./issue-search.component.css']
})
export class IssueSearchComponent implements OnInit {

  private statusFilter: string;
  private typeFilter: string;
  private assigneeFilter: User;
  private reporterFilter: User;
  private projectFilter: Project;

  private users: User[];
  private projects: Project[];
  private statuses: string[];
  private types: string[];

  private issues: Issue[];

  constructor(
    private issueService: IssueService,
    private projectService: ProjectService,
    private userService: UserService
  ) {
  }

  ngOnInit() {
    this.getUsers();
    this.getProjects();
    this.getStatuses();
    this.getTypes();
  }


  private getTypes() {
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

  private getStatuses() {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  private onSearchClick() {
    this.projectService.getIssues({
      status: this.statusFilter ? this.statusFilter : '',
      type: this.typeFilter ? this.typeFilter : '',
      assigneeId: this.assigneeFilter ? this.assigneeFilter : '',
      reporterId: this.reporterFilter ? this.reporterFilter : '',
      projectId: this.projectFilter ? this.projectFilter : ''
    }).subscribe(issues =>
      this.issues = issues
    );
  }
}
