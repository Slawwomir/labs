import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IssueService} from "../issue.service";
import {Issue} from "../issue";

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

  constructor(
    private issueService: IssueService
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
}
