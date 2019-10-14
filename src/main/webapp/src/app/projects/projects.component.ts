import {Component, OnInit} from '@angular/core';
import {ProjectService} from "../project.service";
import {Project} from "../project";
import {AppComponent} from "../app.component";
import {Auth} from "../utils/auth";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})

export class ProjectsComponent implements OnInit {

  projects: Project[];
  errors: String[];

  constructor(private projectService: ProjectService) {
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
        error.error.parameterViolations.forEach(err => {
          this.errors[err.path.split(".")[2]] = err.message;
        });
      });
  }

  add(name: string): void {
    name = name.trim();
    if (!name) return;

    this.projectService.createProject({name, projectOwnerId: Auth.getCurrentUser().id} as Project)
      .subscribe(project => {
        this.getProjects();
        this.errors = [];
      }, error => {
        error.error.parameterViolations.forEach(err => {
          this.errors[err.path.split(".")[2]] = err.message;
        })
      });
  }

  remove(project: Project): void {
    this.projectService.removeProject(project.id)
      .subscribe(_ => this.getProjects());
  }
}
