import {Component, OnInit} from '@angular/core';
import {ProjectService} from "../../services/project.service";
import {Project} from "../../models/project";
import {Auth} from "../../shared/utils/auth";
import {ValidationUtils} from "../../shared/utils/validationUtils";
import {AuthService} from "../../services/auth.service";
import {Role} from "../../models/role";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})

export class ProjectsComponent implements OnInit {

  projects: Project[];
  errors: String[];
  RoleEnum = Role;

  constructor(private projectService: ProjectService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.getProjects();
    this.errors = [];
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
    this.projectService.createProject({name, projectOwnerId: Auth.getCurrentUser().id} as Project)
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
}
