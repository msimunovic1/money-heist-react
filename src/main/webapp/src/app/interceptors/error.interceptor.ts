import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {tap} from "rxjs/operators";
import {NbToastrService} from "@nebular/theme";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(public toastrService: NbToastrService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return next.handle(request)
      .pipe(
        tap(() => {},
          (e: HttpErrorResponse) => {
            if(e.error.message) {
              this.toastrService.danger(e.error.message);
            } else {
              this.toastrService.danger("Something going wrong. Please contact admin.");
            }
          })

      );
  }
}
