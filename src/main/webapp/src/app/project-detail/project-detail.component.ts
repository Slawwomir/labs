import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";

import {ProjectService} from "../project.service";
import {Project} from "../project";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  project: Project;
  projectEdit: Project;
  editMode: boolean;
  errors: string[];

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.getProject();
    this.errors = [];
  }

  getProject(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projectService.getProject(id)
      .subscribe(project => this.project = project);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.projectService.updateProject(this.projectEdit)
      .subscribe(project => {
        this.project = project;
        this.editMode = false;
      }, error => {
        this.mapErrors(error);
      });
  }

  edit(): void {
    this.editMode = true;
    this.errors = [];
    this.projectEdit = Object.assign({}, this.project);
  }


  private mapErrors(error) {
    if (error instanceof HttpErrorResponse) {
      const errorMessages = [];
      error.error.parameterViolations.forEach(err => {
        errorMessages[err.path.split(".")[2]] = err.message;
      });

      this.errors = errorMessages;
    }
  }
}
