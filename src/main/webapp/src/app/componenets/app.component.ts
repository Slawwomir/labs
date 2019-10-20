import {Component} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {Role} from "../models/role";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Projects Management';
  RoleEnum = Role;

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
