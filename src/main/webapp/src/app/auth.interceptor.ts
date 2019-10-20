import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {authResult} from "./models/authResult";
import {catchError} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private router: Router
  ) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const authResult = localStorage.getItem("auth_result");


    if (authResult) {
      let token = (JSON.parse(authResult) as authResult).token;
      let requestWithAuth = req.clone({
        headers: req.headers.set("Authorization", "Bearer " + token)
      });

      return next.handle(requestWithAuth).pipe(
        catchError(err => {
          if (err instanceof HttpErrorResponse && err.status == 511) {
            localStorage.removeItem("auth_result");
            throwError("Token was invalid. User is logged out");
            this.router.navigateByUrl("/login");
          }
          return throwError(err);
        })
      );
    } else {
      return next.handle(req);
    }
  }

}
