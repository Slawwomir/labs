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
  }

  private getIssue(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.issueService.getIssue(id)
      .subscribe(issue => {
        this.issue = issue;
      });
  }

  save(issue: Issue) {
    this.editMode = false;
    this.issue = issue;
  }
}
