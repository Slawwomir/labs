import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {ProjectsComponent} from './projects/projects.component';
import {FormsModule} from "@angular/forms";
import {ProjectDetailComponent} from './projects/project-detail/project-detail.component';
import {AppRoutingModule} from './app-routing.module';
import {DashboardComponent} from './dashboard/dashboard.component';
import {HttpClientModule} from "@angular/common/http";
import {IssueListComponent} from './projects/project-detail/issue-list/issue-list.component';
import {IssueViewComponent} from './projects/project-detail/issue-list/issue-view/issue-view.component';
import {CommonModule} from "@angular/common";
import {IssueEditComponent} from './projects/project-detail/issue-list/issue-edit/issue-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    ProjectsComponent,
    ProjectDetailComponent,
    DashboardComponent,
    IssueListComponent,
    IssueViewComponent,
    IssueEditComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
