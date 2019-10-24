import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Project} from "../../../../models/project";
import {ProjectService} from "../../../../services/project.service";
import {Issue} from "../../../../models/issue";
import {IssueService} from "../../../../services/issue.service";
import {Location} from "@angular/common";
import {ValidationUtils} from "../../../../shared/utils/validationUtils";
import {AuthService} from "../../../../services/auth.service";
import {PermissionsService} from "../../../../services/permissions.service";
import {PermissionLevel} from "../../../../models/permissionLevel";
import {WebsocketService} from "../../../../services/websocket.service";
import {UserService} from "../../../../services/user.service";
import {User} from "../../../../models/user";

@Component({
  selector: 'app-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.css']
})
export class IssueListComponent implements OnInit, OnChanges, OnDestroy {

  private static DEFAULT_SELECT = "All";

  @Input()
  projectId: number;
  issues: Issue[];
  statuses: string[];
  errorMessages: String[];
  filterByStatus: String = IssueListComponent.DEFAULT_SELECT;
  addIssuePermissions: PermissionLevel[];
  editIssuePermissions: PermissionLevel[];
  removeIssuePermissions: PermissionLevel[];
  users: Map<number, User> = new Map<number, User>();

  isFullCreate: boolean;
  newIssue: Issue;

  subject;

  constructor(
    private projectService: ProjectService,
    private issueService: IssueService,
    private userService: UserService,
    private permissionsService: PermissionsService,
    private authService: AuthService,
    private webSocket: WebsocketService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.errorMessages = [];
    this.getIssues();
    this.getStatuses();

    this.permissionsService.getUserPermissionsForAction("addIssue")
      .subscribe(permissions =>
        this.addIssuePermissions = permissions
      );

    this.permissionsService.getUserPermissionsForAction("removeIssue")
      .subscribe(permissions =>
        this.removeIssuePermissions = permissions
      );

    this.watchIssues();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.projectId = changes.projectId.currentValue;
    this.getIssues();
  }

  ngOnDestroy(): void {
    this.webSocket.disconnect(this.subject);
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

  canRemoveIssue(reporterId: number) {
    return ValidationUtils.validatePermissions(this.removeIssuePermissions, reporterId, this.authService.getUserId());
  }

  canAddIssue() {
    return ValidationUtils.validatePermissions(this.addIssuePermissions);
  }

  private getIssues(): void {
    let filters: {};

    if (this.filterByStatus != IssueListComponent.DEFAULT_SELECT) {
      filters = {status: this.filterByStatus};
    }

    this.projectService.getIssues({id: this.projectId} as Project, filters)
      .subscribe(issues => {
        this.issues = issues;
        this.fetchUsers();
      });
  }

  private fetchUsers() {
    this.permissionsService.getUserPermissionsForAction("getUser")
      .subscribe(response => {
          if (ValidationUtils.validatePermissions(response)) {
            this.issues.forEach(issue => this.getReporter(issue.reporterId))
          }
        }
      );
  }

  private getStatuses(): void {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  private watchIssues() {
    this.subject = this.webSocket.connect(this.projectId);
    this.subject.onMessage = (message => {
      this.getIssues();
    });
    this.subject.subscribe(m => {
      this.getIssues();
    })
  }

  private getReporter(reporterId: number) {
    this.userService.getUser(reporterId)
      .subscribe(user =>
        this.users.set(reporterId, user)
      )
  }
}
