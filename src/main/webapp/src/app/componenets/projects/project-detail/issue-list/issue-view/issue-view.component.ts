import {Component, OnInit} from '@angular/core';
import {Issue} from "../../../../../models/issue";
import {IssueService} from "../../../../../services/issue.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {User, UserService} from "../../../../../services/user.service";

@Component({
  selector: 'app-issue-view',
  templateUrl: './issue-view.component.html',
  styleUrls: ['./issue-view.component.css']
})
export class IssueViewComponent implements OnInit {

  issue: Issue;
  editMode: boolean;
  assignee: User;

  constructor(
    private location: Location,
    private issueService: IssueService,
    private userService: UserService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.getIssue();
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

  save(issue: Issue) {
    this.editMode = false;
    this.issue = issue;
    this.getAssignee(issue);
  }
}
