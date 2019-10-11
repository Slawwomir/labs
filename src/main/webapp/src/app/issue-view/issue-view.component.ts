import {Component, OnInit} from '@angular/core';
import {Issue} from "../issue";
import {IssueService} from "../issue.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";

@Component({
  selector: 'app-issue-view',
  templateUrl: './issue-view.component.html',
  styleUrls: ['./issue-view.component.css']
})
export class IssueViewComponent implements OnInit {

  issue: Issue;
  issueEdit: Issue;
  editMode: boolean;
  statuses: string[];
  types: string[];

  constructor(
    private location: Location,
    private issueService: IssueService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.getIssue();
  }

  openEditMode(): void {
    this.editMode = true;
    this.issueEdit = Object.assign({}, this.issue);

    if (!this.statuses) {
      this.getStatuses();
    }

    if (!this.types) {
      this.getIssueTypes();
    }
  }

  save(): void {
    this.editMode = false;

    this.issueService.updateIssue(this.issueEdit)
      .subscribe(issue => this.issue = issue);
  }

  private getStatuses(): void {
    this.issueService.getStatuses()
      .subscribe(statuses => this.statuses = statuses);
  }

  private getIssue(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.issueService.getIssue(id)
      .subscribe(issue => {
        this.issue = issue;
      });
  }

  private getIssueTypes() {
    this.issueService.getIssueTypes()
      .subscribe(issueTypes => this.types = issueTypes);
  }
}
