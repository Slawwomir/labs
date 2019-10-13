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

  constructor(private projectService: ProjectService) {
  }

  ngOnInit() {
    this.getProjects();
  }

  getProjects(): void {
    this.projectService.getProjects()
      .subscribe(projects => this.projects = projects);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) return;

    this.projectService.createProject({name, projectOwnerId: Auth.getCurrentUser().id} as Project)
      .subscribe(project => {
        this.projects.push(project)
      })
  }

  remove(project: Project): void {
    this.projectService.removeProject(project.id)
      .subscribe(_ => this.getProjects());
  }
}
