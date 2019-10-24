import {Component} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {Role} from "../models/role";
import {PermissionLevel} from "../models/permissionLevel";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Projects Management';
  RoleEnum = Role;
  PermissionLevelsEnum = PermissionLevel;

  private canSeeProjects: boolean;

  constructor(
    private authService: AuthService,
    router: Router
  ) {
    if (authService.isLoggedIn()) {
      router.navigate(['dashboard']);
    } else {
      router.navigate(['login']);
    }
  }
}
