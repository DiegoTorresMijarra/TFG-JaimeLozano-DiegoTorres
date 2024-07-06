import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { environment as envProd } from '../../environments/environment.prod';
import {StatusResponseEntity} from "../models/statusResponse.entity";

@Injectable({
  providedIn: 'root'
})
export class BackStatusService {
  private apiUrl = (environment.production ? envProd.apiUrl : environment.apiUrl) + 'app-status/badge-status';

  constructor(private http: HttpClient) {}

  getBadgeStatus(): Observable<StatusResponseEntity> {
    return this.http.get<StatusResponseEntity>(this.apiUrl).pipe(
      catchError((error) => {
        console.error('Error obteniendo estado de la aplicación:', error);
        let errorMessage = 'unknown error';
        if (error.status) {
          switch (error.status) {
            case 404:
              errorMessage = '404 Not Found';
              break;
            case 500:
              errorMessage = '500 Internal Server Error';
              break;
            case 503:
              errorMessage = '503 Service Unavailable';
              break;
            default:
              errorMessage = `${error.status} ${error.statusText}`;
          }
        }
        return of(new StatusResponseEntity(1, 'Status', errorMessage))
      })
    );
  }
}
