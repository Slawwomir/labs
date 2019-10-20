import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = localStorage.getItem("token");

    if (token) {
      let requestWithAuth = req.clone({
        headers: req.headers.set("Authorization", "Bearer " + token)
      });

      return next.handle(requestWithAuth);
    } else {
      return next.handle(req);
    }
  }

}