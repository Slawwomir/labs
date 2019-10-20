import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {User} from "../../models/user";
import {Role} from "../../models/role";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {

  users: User[];
  roles: Role[];

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) {
  }

  ngOnInit() {
    this.getUsers();
    this.roles = Object.values(Role);
  }


  private getUsers() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
    });
  }

  updateUser(user: User) {
    this.userService.updateUser(user)
      .subscribe(user => {
        this.getUsers();
        this.router.navigateByUrl("/");
      })
  }
}
