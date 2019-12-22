import {Component, OnInit} from '@angular/core';
import {Issue} from "../../../../../models/issue";
import {IssueService} from "../../../../../services/issue.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {UserService} from "../../../../../services/user.service";
import {User} from "../../../../../models/user";
import {PermissionsService} from "../../../../../services/permissions.service";
import {PermissionLevel} from "../../../../../models/permissionLevel";
import {ValidationUtils} from "../../../../../shared/utils/validationUtils";
import {AuthService} from "../../../../../services/auth.service";

@Component({
  selector: 'app-issue-view',
  templateUrl: './issue-view.component.html',
  styleUrls: ['./issue-view.component.css']
})
export class IssueViewComponent implements OnInit {

  issue: Issue;
  editMode: boolean;
  assignee: User;
  reporter: User;
  editIssuePermissions: PermissionLevel[];

  constructor(
    private location: Location,
    private issueService: IssueService,
    private userService: UserService,
    private permissionsService: PermissionsService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.getIssue();
    this.permissionsService.getUserPermissionsForAction("updateIssue")
      .subscribe(permissions =>
        this.editIssuePermissions = permissions
      );
  }

  openEditMode(): void {
    this.editMode = true;
  }

  private getIssue(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.issueService.getIssue(id)
      .subscribe(issue => {
        this.issue = issue;
        this.getAssignee(issue);
        this.getReporter(issue);
      });
  }

  private getAssignee(issue) {
    if (issue.assigneeId) {
      this.userService.getUser(issue.assigneeId)
        .subscribe(assignee => {
          this.assignee = assignee;
        })
    } else {
      this.assignee = {id: null, username: "Unassigned"} as User;
    }
  }

  private getReporter(issue) {
    this.userService.getUser(issue.reporterId)
      .subscribe(reporter => {
        this.reporter = reporter;
      })
  }

  save(issue: Issue) {
    this.editMode = false;
    this.issue = issue;
    this.getAssignee(issue);
  }

  canEditIssue(reportedId: number) {
    return ValidationUtils.validatePermissions(this.editIssuePermissions, reportedId, this.authService.getUserId());
  }
}
