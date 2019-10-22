import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";

import {ProjectService} from "../../../services/project.service";
import {Project} from "../../../models/project";
import {ValidationUtils} from "../../../shared/utils/validationUtils";
import {AuthService} from "../../../services/auth.service";
import {Role} from "../../../models/role";
import {PermissionsService} from "../../../services/permissions.service";
import {PermissionLevel} from "../../../models/permissionLevel";

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
  updateProjectPermissions: PermissionLevel[];
  getIssuesPermissions: PermissionLevel[];
  addIssuePermissions: PermissionLevel[];

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private authService: AuthService,
    private permissionsService: PermissionsService,
    private location: Location
  ) {
  }

  ngOnInit() {
    this.getProject();
    this.errors = [];
    this.route.params.subscribe(queryParams => {
      this.getProject();
    });

    this.permissionsService.getUserPermissionsForAction("updateProject")
      .subscribe(permissions =>
        this.updateProjectPermissions = permissions
      );

    this.permissionsService.getUserPermissionsForAction("getIssue")
      .subscribe(permissions =>
        this.getIssuesPermissions = permissions
      );

    this.permissionsService.getUserPermissionsForAction("addIssue")
      .subscribe(permissions =>
        this.addIssuePermissions = permissions
      );
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

  canUpdateProject() {
    return ValidationUtils.validatePermissions(
      this.updateProjectPermissions, this.project.projectOwnerId, this.authService.getUserId()
    )
  }

  canSeeOrAddIssues() {
    return ValidationUtils.validatePermissions(this.getIssuesPermissions)
      || ValidationUtils.validatePermissions(this.addIssuePermissions);
  }
}
