import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './componenets/app.component';
import {ProjectsComponent} from './componenets/projects/projects.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ProjectDetailComponent} from './componenets/projects/project-detail/project-detail.component';
import {AppRoutingModule} from './app-routing.module';
import {DashboardComponent} from './componenets/dashboard/dashboard.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {IssueListComponent} from './componenets/projects/project-detail/issue-list/issue-list.component';
import {IssueViewComponent} from './componenets/projects/project-detail/issue-list/issue-view/issue-view.component';
import {CommonModule} from "@angular/common";
import {IssueEditComponent} from './componenets/projects/project-detail/issue-list/issue-edit/issue-edit.component';
import {LoginComponent} from './componenets/login/login.component';
import {AuthInterceptor} from "./auth.interceptor";
import {RolesComponent} from "./componenets/admin-panel/roles/roles.component";
import {AccountManagementComponent} from './componenets/account-management/account-management.component';
import {RegistrationComponent} from './componenets/registration/registration.component';
import {AdminPanelComponent} from './componenets/admin-panel/admin-panel.component';
import {PermissionsComponent} from './componenets/admin-panel/permissions/permissions.component';

@NgModule({
  declarations: [
    AppComponent,
    ProjectsComponent,
    ProjectDetailComponent,
    DashboardComponent,
    IssueListComponent,
    IssueViewComponent,
    IssueEditComponent,
    LoginComponent,
    RolesComponent,
    AccountManagementComponent,
    RegistrationComponent,
    AdminPanelComponent,
    PermissionsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
