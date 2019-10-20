import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {ProjectsComponent} from "./componenets/projects/projects.component";
import {DashboardComponent} from "./componenets/dashboard/dashboard.component";
import {ProjectDetailComponent} from "./componenets/projects/project-detail/project-detail.component";
import {IssueViewComponent} from "./componenets/projects/project-detail/issue-list/issue-view/issue-view.component";
import {LoginComponent} from "./componenets/login/login.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'projects', component: ProjectsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'issue/:id', component: IssueViewComponent},
  {path: 'projects/:id', component: ProjectDetailComponent},
  {path: 'login', component: LoginComponent}
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
