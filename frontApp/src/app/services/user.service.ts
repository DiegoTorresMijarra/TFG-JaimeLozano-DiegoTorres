import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { AuthService } from './auth.service'
import { Observable } from 'rxjs'
import { UserResponseDto } from '../models/user.entity'

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
}
