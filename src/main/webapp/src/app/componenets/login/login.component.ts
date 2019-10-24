import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  form: FormGroup;
  error: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })
  }

  login() {
    const credentials = this.form.value;

    if (credentials.username && credentials.password) {
      this.authService.login(credentials.username, credentials.password)
        .subscribe(() => {
          console.log("User is logged in");
          this.router.navigateByUrl("/dashboard")
        }, error => {
          this.error = "Credentials are invalid";
        })
    }
  }
}
