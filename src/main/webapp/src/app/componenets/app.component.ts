import {Component} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {Role} from "../models/role";
import {PermissionsService} from "../services/permissions.service";
import {PermissionLevel} from "../models/permissionLevel";
import {ValidationUtils} from "../shared/utils/validationUtils";

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
    private permissionsService: PermissionsService,
    router: Router
  ) {
    if (authService.isLoggedIn()) {
      permissionsService.getUserPermissionsForAction("getProject")
        .subscribe(permissions => {
          this.canSeeProjects = ValidationUtils.validatePermissions(permissions);
        });
      router.navigate(['dashboard']);
    } else {
      router.navigate(['login']);
    }
  }
}
