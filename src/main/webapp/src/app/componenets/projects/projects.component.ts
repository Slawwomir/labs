import {Component, OnInit} from '@angular/core';
import {ProjectService} from "../../services/project.service";
import {Project} from "../../models/project";
import {ValidationUtils} from "../../shared/utils/validationUtils";
import {AuthService} from "../../services/auth.service";
import {Role} from "../../models/role";
import {PermissionsService} from "../../services/permissions.service";
import {PermissionLevel} from "../../models/permissionLevel";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})

export class ProjectsComponent implements OnInit {

  projects: Project[];
  errors: String[];
  RoleEnum = Role;
  private createProjectPermissions: PermissionLevel[];
  private removeProjectPermissions: PermissionLevel[];

  constructor(private projectService: ProjectService,
              private permissionsService: PermissionsService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.getProjects();
    this.errors = [];

    this.permissionsService.getUserPermissionsForAction("addProject")
      .subscribe(permissions =>
        this.createProjectPermissions = permissions
      );

    this.permissionsService.getUserPermissionsForAction("removeProject")
      .subscribe(permissions =>
        this.removeProjectPermissions = permissions
      );
  }

  getProjects(): void {
    this.projectService.getProjects()
      .subscribe(projects => {
        this.projects = projects;
        this.errors = [];
      }, error => {
        this.errors = ValidationUtils.mapErrors(error);
      });
  }

  add(name: string): void {
    this.projectService.createProject({name, projectOwnerId: this.authService.getUserId()} as Project)
      .subscribe(project => {
        this.getProjects();
        this.errors = [];
      }, error => {
        this.errors = ValidationUtils.mapErrors(error);
      });
  }

  remove(project: Project): void {
    this.projectService.removeProject(project.id)
      .subscribe(_ => this.getProjects());
  }

  canRemoveProject(projectOwnerId: number) {
    return ValidationUtils.validatePermissions(
      this.removeProjectPermissions, projectOwnerId, this.authService.getUserId()
    );
  }

  canCreateProject() {
    return ValidationUtils.validatePermissions(this.createProjectPermissions);
  }
}
