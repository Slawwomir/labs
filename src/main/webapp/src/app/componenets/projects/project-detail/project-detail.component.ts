import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";

import {ProjectService} from "../../../services/project.service";
import {Project} from "../../../models/project";
import {ValidationUtils} from "../../../shared/utils/validationUtils";
import {AuthService} from "../../../services/auth.service";
import {Role} from "../../../models/role";

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  project: Project;
  projectEdit: Project;
  editMode: boolean;
  errors: String[];
  RoleEnum = Role;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private authService: AuthService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.getProject();
    this.errors = [];
    this.route.params.subscribe(queryParams => {
      this.getProject();
    })
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
        this.errors = ValidationUtils.mapErrors(error);
      });
  }

  edit(): void {
    this.editMode = this.authService.isUserInRole(Role.Admin);
    this.errors = [];
    this.projectEdit = Object.assign({}, this.project);
  }
}
