import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { AuthService } from './auth.service'
import { catchError, Observable, throwError } from 'rxjs'
import { User, UserResponseDto } from '../models/user.entity'
import { Order } from '../models/order.entity'
import { environment } from '../../environments/environment'
import { environment as envProd } from '../../environments/environment.prod'

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl =
    (environment.production ? envProd.apiUrl : environment.apiUrl) + 'users'

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
