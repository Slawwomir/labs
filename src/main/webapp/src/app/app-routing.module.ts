import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {ProjectsComponent} from "./projects/projects.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {ProjectDetailComponent} from "./projects/project-detail/project-detail.component";
import {IssueViewComponent} from "./projects/project-detail/issue-list/issue-view/issue-view.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'projects', component: ProjectsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'issue/:id', component: IssueViewComponent},
  {path: 'projects/:id', component: ProjectDetailComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule {
}
