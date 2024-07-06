import { Injectable } from '@angular/core'
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http'
import { Observable, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators'
import { Router } from '@angular/router'
import {environment} from "../../environments/environment";
import {environment as envProd} from "../../environments/environment.prod";

@Injectable()
export class HttpErrorService implements HttpInterceptor {
  private apiUrl =
    (environment.production ? envProd.apiUrl : environment.apiUrl)

  constructor(private router: Router) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {

    const excludedUrls : Set<string> = new Set<string>([this.apiUrl + 'auth/signin', this.apiUrl +'app-status/badge-status'])
    if (excludedUrls.has(req.url)) {
      return next.handle(req)
    }

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('HTTP error occurred:', error)
        if (error.status === 400) {
          this.router.navigate(['/error/400'])
        } else if (error.status === 403) {
          this.router.navigate(['/error/403'])
        } else if (error.status === 404) {
          this.router.navigate(['/error/404'])
        } else if (error.status === 500) {
          this.router.navigate(['/error/404'])
        } else if (error.status === 401) {
          this.router.navigate(['/login'])
        }
        return throwError(() => new Error(error.message))
      }),
    )
  }
}
