import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class HttpErrorService implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: any) => {
        if (error.status === 400) {
          this.router.navigate(['/error/400']);
        } else if (error.status === 403) {
          this.router.navigate(['/error/403']);
        } else if (error.status === 404) {
          this.router.navigate(['/error/404']);
        } else if (error.status === 500) {
          console.log(error)
          this.router.navigate(['/error/404']);
        } else if (error.status === 401) {
          this.router.navigate(['/login']);
        }
        return throwError(() => new Error(error.message));
      })
    );
  }

}
