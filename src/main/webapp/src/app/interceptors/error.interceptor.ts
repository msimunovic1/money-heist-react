import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {tap} from "rxjs/operators";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return next.handle(request)
      .pipe(
        tap(() => {},
          (e: HttpErrorResponse) => {
            if(e.error.message) {
              // TODO: staviti karticu s porukom korisniku
              console.log(e.error.message)
            } else {
              console.log("Error occured application. Please contact admin")
            }
          })

      );
  }
}
