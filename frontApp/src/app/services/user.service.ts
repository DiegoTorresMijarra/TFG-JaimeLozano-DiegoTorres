import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { AuthService } from './auth.service'
import {catchError, Observable, throwError} from 'rxjs'
import {User, UserResponseDto} from '../models/user.entity'
import {Order} from "../models/order.entity";

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'https://localhost:3000/v1/users'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  meDetails(): Observable<UserResponseDto> {
    const headers = this.authService.getAuthHeaders()

    return this.http.get<UserResponseDto>(`${this.apiUrl}/me/details`, {
      headers,
    })
  }

  getUser(id: string): Observable<User> {
    const headers = this.authService.getAuthHeaders()
    return this.http.get<User>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo usuario:', error)
        return throwError(error)
      }),
    )
  }
}
