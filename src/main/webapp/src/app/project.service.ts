import {Injectable} from '@angular/core';
import {Project} from "./project";
import {PROJECTS} from "./mock-projects";
import {Observable, of} from "rxjs";
import {MessageService} from "./message.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private projectsUrl = 'localhost:8080';

  constructor(
    private httpClient: HttpClient,
    private messageService: MessageService
  ) {
  }

  getProjects(): Observable<Project[]> {
    this.messageService.add('ProjectService: fetched projects');
    return of(PROJECTS);
  }

  getProject(id: number) {
    this.messageService.add(`ProjectService: fetched project id=${id}`);
    return of(PROJECTS.find(project => project.id == id));
  }

  private log(message: string) {
    this.messageService.add(`ProjectService: ${message}`);
  }
}
