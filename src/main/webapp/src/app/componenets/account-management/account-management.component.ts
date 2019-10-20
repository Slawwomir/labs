import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-account-management',
  templateUrl: './account-management.component.html',
  styleUrls: ['./account-management.component.css']
})
export class AccountManagementComponent implements OnInit {

  form: FormGroup;
  error: String;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.formBuilder.group({
      password_1: ['', Validators.required],
      password_2: ['', Validators.required]
    })
  }

  ngOnInit() {
  }

  changePassword() {
    const passwords = this.form.value;

    if (passwords.password_1 !== passwords.password_2) {
      this.error = "Passwords are not identical";
      return;
    }

    this.authService.changePassword(passwords.password_1)
      .subscribe(_ => {
        this.router.navigateByUrl("/");
      });
  }
}
