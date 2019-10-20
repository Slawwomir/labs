import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Project} from "../../../../models/project";
import {ProjectService} from "../../../../services/project.service";
import {Issue} from "../../../../models/issue";
import {IssueService} from "../../../../services/issue.service";
import {Location} from "@angular/common";
import {ValidationUtils} from "../../../../shared/utils/validationUtils";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit, OnChanges {

  private static DEFAULT_SELECT = "All";

  @Input()
  projectId: number;
  issues: Issue[];
  statuses: string[];
  errorMessages: String[];
  filterByStatus: String = IssueListComponent.DEFAULT_SELECT;

  isFullCreate: boolean;
  newIssue: Issue;

  constructor(
    private projectService: ProjectService,
    private issueService: IssueService,
    private authService: AuthService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.errorMessages = [];
    this.getIssues();
    this.getStatuses();
  }

  add(issueName: string): void {
    this.issueService.createIssue({
      name: issueName,
      projectId: this.projectId,
      reporterId: this.authService.getUserId(),
      status: "OPEN",
      type: "TASK"
    } as Issue)
      .subscribe(issue => {
        this.getIssues();
        this.errorMessages = [];
      }, error => {
        this.errorMessages = ValidationUtils.mapErrors(error);
      });
  }

  fullCreate(issueName: string): void {
    this.isFullCreate = true;
    this.errorMessages = [];
    this.newIssue = {name: issueName, projectId: this.projectId} as Issue;
  }

  save(issue: Issue): void {
    this.isFullCreate = false;
    this.getIssues();
    this.errorMessages = [];
  }

  remove(issue: Issue): void {
    this.issueService.removeIssue(issue)
      .subscribe(_ => this.getIssues());
  }

  goBack(): void {
    this.location.back();
  }

  private getIssues(): void {
    let filters: {};

    if (this.filterByStatus != IssueListComponent.DEFAULT_SELECT) {
      filters = {status: this.filterByStatus};
    }

    this.projectService.getIssues({id: this.projectId} as Project, filters)
      .subscribe(issues => this.issues = issues);
  }

  private getStatuses(): void {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.projectId = changes.projectId.currentValue;
    this.getIssues();
  }
}
