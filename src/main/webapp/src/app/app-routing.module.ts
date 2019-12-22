import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {ProjectsComponent} from "./componenets/projects/projects.component";
import {DashboardComponent} from "./componenets/dashboard/dashboard.component";
import {ProjectDetailComponent} from "./componenets/projects/project-detail/project-detail.component";
import {IssueViewComponent} from "./componenets/projects/project-detail/issue-list/issue-view/issue-view.component";
import {LoginComponent} from "./componenets/login/login.component";
import {AccountManagementComponent} from "./componenets/account-management/account-management.component";
import {RegistrationComponent} from "./componenets/registration/registration.component";
import {AdminPanelComponent} from "./componenets/admin-panel/admin-panel.component";
import {RolesComponent} from "./componenets/admin-panel/roles/roles.component";
import {PermissionsComponent} from "./componenets/admin-panel/permissions/permissions.component";
import {IssueSearchComponent} from "./componenets/issue-search/issue-search.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'projects', component: ProjectsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'issue/:id', component: IssueViewComponent},
  {path: 'projects/:id', component: ProjectDetailComponent},
  {path: 'login', component: LoginComponent},
  {path: 'admin', component: AdminPanelComponent},
  {path: 'admin/roles', component: RolesComponent},
  {path: 'admin/permissions', component: PermissionsComponent},
  {path: 'account', component: AccountManagementComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'search', component: IssueSearchComponent}
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
