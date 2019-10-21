import {Component, OnInit} from '@angular/core';
import {PermissionsService} from "../../../services/permissions.service";
import {Role} from "../../../models/role";
import {Permission} from "../../../models/permission";

@Component({
  selector: 'app-permissions',
  templateUrl: './permissions.component.html',
  styleUrls: ['./permissions.component.css']
})
export class PermissionsComponent implements OnInit {

  roles: Role[];
  permissionLevels: String[];
  methods: String[];

  permissions: Permission[];
  permission: Permission;

  constructor(private permissionsService: PermissionsService) {
  }

  ngOnInit() {
    this.permission = new Permission();
    this.getMethods();
    this.getRoles();
    this.getPermissionLevels();
    this.getPermissions();
  }

  private getPermissions() {
    this.permissionsService.getPermissions()
      .subscribe(permissions => this.permissions = permissions);
  }

  private getRoles() {
    this.permissionsService.getRoles()
      .subscribe(roles => this.roles = roles);
  }

  private getMethods() {
    this.permissionsService.getMethods()
      .subscribe(methods => this.methods = methods);
  }

  private getPermissionLevels() {
    this.permissionsService.getLevels()
      .subscribe(levels => this.permissionLevels = levels);
  }

  private addPermission() {
    this.permissionsService.addPermission(this.permission)
      .subscribe(permission => this.getPermissions());
  }

  private remove(id: number) {
    this.permissionsService.removePermission(id)
      .subscribe(() => {
        this.getPermissions();
      })
  }
}
